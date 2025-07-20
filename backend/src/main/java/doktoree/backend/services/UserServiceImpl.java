package doktoree.backend.services;

import java.beans.Encoder;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import doktoree.backend.domain.Employee;
import doktoree.backend.domain.User;
import doktoree.backend.dtos.UserDto;
import doktoree.backend.error_response.Response;
import doktoree.backend.exceptions.EmptyEntityListException;
import doktoree.backend.exceptions.EntityNotDeletedException;
import doktoree.backend.exceptions.EntityNotExistingException;
import doktoree.backend.exceptions.EntityNotSavedException;
import doktoree.backend.exceptions.InvalidForeignKeyException;
import doktoree.backend.mapper.UserMapper;
import doktoree.backend.repositories.EmployeeRepository;
import doktoree.backend.repositories.UserRepository;

import javax.swing.text.html.Option;

@Service
public class UserServiceImpl implements UserService{

	private final UserRepository userRepository;

	private final EmployeeRepository employeeRepository;
	
	@Autowired
	public UserServiceImpl(UserRepository userRepository, EmployeeRepository employeeRepository) {
		this.userRepository = userRepository;
		this.employeeRepository = employeeRepository;
	}

	@Override
	public Response<UserDto> findUserById(Long id) throws EntityNotExistingException {
		Optional<User> optionalUser = userRepository.findById(id);
		Response<UserDto> response = new Response<>();
		
		
		if(optionalUser.isPresent()) {
			
			UserDto userDto = UserMapper.mapToUserDto(optionalUser.get());
			response.setDto(userDto);
			response.setMessage("User found successfully!");
			return response;
			
		}
			
		
		throw new EntityNotExistingException("There is not user with given ID!");
	}

	@Override
	public Response<UserDto> saveUser(UserDto dto) throws EntityNotSavedException {
		User user = UserMapper.mapToUser(dto);
		Response<UserDto> response = new Response<>();
		employeeRepository.findById(dto.getEmployeeId()).orElseThrow(() -> new InvalidForeignKeyException("There is no employee with given ID!"));
		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		user.setPassword(passwordEncoder.encode(user.getPassword()));

		validateUser(user);

		try {
			User savedUser = userRepository.save(user);
			response.setDto(UserMapper.mapToUserDto(savedUser));
			response.setMessage("User successfully saved!");
			return response;
		} catch (Exception e) {
			throw new EntityNotSavedException("User can not be saved!");
		}
	}

	@Override
	public Response<UserDto> deleteUser(Long id) throws EntityNotExistingException, EntityNotDeletedException {

		Optional<User> optionalUser = userRepository.findById(id);
		
		if(optionalUser.isEmpty())
			throw new EntityNotExistingException("There is not user with given ID!");
		
		try {
			userRepository.deleteById(id);
			User user = optionalUser.get();
			UserDto dto = UserMapper.mapToUserDto(user);
			Response<UserDto> response = new Response<>();
			response.setDto(dto);
			response.setMessage("User successfully deleted!");
			return response;
		} catch (Exception e) {
			throw new EntityNotDeletedException("User can not be deleted");
		}
	}

	@Override
	public Response<List<UserDto>> getAllUsers(int pageNumber) throws EmptyEntityListException {

		Page<User> usersPage = userRepository.findAll(PageRequest.of(pageNumber, 10));
		List<User> users = usersPage.getContent();
		
		if(users.isEmpty())
			throw new EmptyEntityListException("There are no users!");
		
		List<UserDto> userDtos =  users.stream().map(UserMapper::mapToUserDto).collect(Collectors.toList());
		Response<List<UserDto>> response = new Response<>();
		response.setDto(userDtos);
		response.setMessage("All users found successfully!");
		return response;
	}

	@Override
	public Response<UserDto> updateUser(UserDto dto) throws EntityNotExistingException, EntityNotSavedException {

		User user = userRepository.findById(dto.getId()).orElseThrow(()-> new EntityNotExistingException("There is not user with given ID!"));
		
		try {
			Employee employee = new Employee();
			employee.setId(dto.getEmployeeId());
			user.setEmail(dto.getEmail());
			user.setPassword(dto.getPassword());
			user.setRole(dto.getRole());
			user.setEmployee(employee);
			userRepository.save(user);
			UserDto userDto = UserMapper.mapToUserDto(user);
			Response<UserDto> response = new Response<>();
			response.setDto(userDto);
			response.setMessage("User successfully updated!");
			return response;
			
		} catch (Exception e) {
			throw new EntityNotSavedException("User can not be updated!");
		}
	}

	@Override
	public User validateUser(User user) throws EntityNotSavedException {

		Optional<User> searchedUser = userRepository.findUserByEmployeeIdAndRole(user.getEmployee().getId(), user.getRole());

		if(searchedUser.isPresent())
			throw new EntityNotSavedException("A user with that employee ID and role already exists!");

		Optional<User> emailUser = userRepository.findByEmail(user.getEmail());

		if(emailUser.isPresent())
			throw new EntityNotSavedException("A user with that email already exists!");


		return user;
	}


}
