package doktoree.backend.service;

import doktoree.backend.domain.Employee;
import doktoree.backend.domain.User;
import doktoree.backend.dtos.UserDto;
import doktoree.backend.enums.Role;
import doktoree.backend.errorresponse.Response;
import doktoree.backend.exceptions.EmptyEntityListException;
import doktoree.backend.exceptions.EntityNotExistingException;
import doktoree.backend.exceptions.EntityNotSavedException;
import doktoree.backend.exceptions.InvalidForeignKeyException;
import doktoree.backend.mapper.UserMapper;
import doktoree.backend.repositories.EmployeeRepository;
import doktoree.backend.repositories.UserRepository;
import doktoree.backend.services.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;

    private Employee employee;

    private List<User> users;

    private User user2;

    public void check(UserDto dto, User user){
        assertThat(dto).isNotNull();
        assertThat(dto.getEmployeeId()).isEqualTo(user.getEmployee().getId());
        assertThat(dto.getPassword()).isEqualTo(user.getPassword());
        assertThat(dto.getId()).isEqualTo(user.getId());
        assertThat(dto.getRole()).isEqualTo(user.getRole());
        assertThat(dto.getEmail()).isEqualTo(user.getEmail());
    }

    @BeforeEach
    public void setup(){

        employee = new Employee();
        employee.setId(11L);

        user = new User();
        user.setId(1L);
        user.setPassword("password");
        user.setRole(Role.USER);
        user.setEmail("mail@gmail.com");
        user.setEmployee(employee);

        user2 =  new User();
        user2.setEmployee(employee);
        user2.setEmail("mejl@gmail.com");
        user2.setPassword("pass");
        user2.setId(22L);
        user.setRole(Role.ADMIN);
        users = List.of(user,user2);

    }

    @DisplayName("Find user by valid ID - should return expected DTO")
    @Test
    public void whenFindUserById_thenReturnExpectedDto(){

        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        Response<UserDto> response = userService.findUserById(user.getId());
        check(response.getDtoT(),user);

    }

    @DisplayName("Find user by invalid ID - should throw EntityNotExistingException")
    @Test
    public void invalidUserId_whenFindUserById_thenThrowException(){

        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> {

            userService.findUserById(user.getId());

        }).isInstanceOf(EntityNotExistingException.class)
                .hasMessageContaining("There is not user with given ID!");

    }

    @DisplayName("Save user - should return expected DTO")
    @Test
    public void whenSaveUser_thenReturnExpectedDto(){

        Mockito.when(employeeRepository.findById(user.getEmployee().getId())).thenReturn(Optional.of(employee));
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(user);
        Response<UserDto> response = userService.saveUser(UserMapper.mapToUserDto(user));
        check(response.getDtoT(), user);


    }

    @DisplayName("Save user by invalid ID - should throw InvalidForeignKeyException")
    @Test
    public void invalidEmployeeId_whenSaveUser_thenThrowException(){

        Mockito.when(employeeRepository.findById(employee.getId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> {

            userService.saveUser(UserMapper.mapToUserDto(user));

        }).isInstanceOf(InvalidForeignKeyException.class)
                .hasMessageContaining("There is no employee with given ID!");

    }

    @DisplayName("Save user - should throw EntityNotSavedException")
    @Test
    public void whenSaveUser_thenThrowException(){

        Mockito.when(employeeRepository.findById(employee.getId())).thenReturn(Optional.of(employee));
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenThrow(new EntityNotSavedException("User can not be saved!"));
        assertThatThrownBy(() -> {

            userService.saveUser(UserMapper.mapToUserDto(user));

        }).isInstanceOf(EntityNotSavedException.class)
                .hasMessageContaining("User can not be saved!");

    }

    @DisplayName("Delete user - should return expected DTO")
    @Test
    public void whenDeleteUser_thenReturnExpectedDto(){

        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        Response<UserDto> response = userService.deleteUser(user.getId());
        check(response.getDtoT(), user);

    }

    @DisplayName("Delete user by invalid ID - should return throw EntityNotExistingException")
    @Test
    public void invalidUserId_whenDeleteUser_thenThrowException(){

        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.empty());
        assertThatThrownBy(() ->{

            userService.deleteUser(user.getId());

        }).isInstanceOf(EntityNotExistingException.class)
                .hasMessageContaining("There is not user with given ID!");


    }

    @DisplayName("Get all users - should return expected DTO")
    @Test
    public void whenGetAllUsers_thenReturnExpectedDto(){

        Page<User> page = new PageImpl<>(users);
        Mockito.when(userRepository.findAll(PageRequest.of(1,10))).thenReturn(page);
        Response<List<UserDto>> response = userService.getAllUsers(1);
        check(response.getDtoT().get(0), user);
        check(response.getDtoT().get(1), user2);

    }

    @DisplayName("Get all users - should return throw EmptyEntityListException")
    @Test
    public void whenGetAllUsers_thenThrowException(){

        Page<User> page = new PageImpl<>(new ArrayList<>());
        Mockito.when(userRepository.findAll(PageRequest.of(1,10))).thenReturn(page);

        assertThatThrownBy(() -> {

            userService.getAllUsers(1);

        }).isInstanceOf(EmptyEntityListException.class)
                .hasMessageContaining("There are no users!");

    }

    @DisplayName("Update user - should return expected DTO")
    @Test
    public void whenUpdateUser_thenReturnExpectedDto(){

        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        Response<UserDto> response = userService.updateUser(UserMapper.mapToUserDto(user));
        check(response.getDtoT(),user);
    }

    @DisplayName("Update user - should throw EntityNotExistingException")
    @Test
    public void invalidUserId_whenUpdateUser_thenThrowException(){

        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> {

            userService.updateUser(UserMapper.mapToUserDto(user));


        }).isInstanceOf(EntityNotExistingException.class)
                .hasMessageContaining("There is not user with given ID!");

    }

    @DisplayName("Update user - should throw EntityNotSavedException")
    @Test
    public void whenUpdateUser_thenThrowException(){

        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        Mockito.when(userRepository.save(user)).thenThrow(new EntityNotSavedException("User can not be updated!"));

        assertThatThrownBy(() -> {

            userService.updateUser(UserMapper.mapToUserDto(user));


        }).isInstanceOf(EntityNotSavedException.class)
                .hasMessageContaining("User can not be updated!");

    }

    @DisplayName("Validate user - should return expected user")
    @Test
    public void whenValidateUser_thenReturnExpectedUser(){

        Mockito.when(userRepository.findUserByEmployeeIdAndRole(user.getEmployee().getId(), user.getRole())).thenReturn(Optional.empty());
        Mockito.when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        User resultUser = userService.validateUser(user);
        check(UserMapper.mapToUserDto(resultUser),user);

    }

    @DisplayName("Validate user - should throw EntityNotSavedException")
    @Test
    public void invalidEmployeeIDorRole_whenValidateUser_thenThrowException(){

        Mockito.when(userRepository.findUserByEmployeeIdAndRole(user.getEmployee().getId(), user.getRole())).thenReturn(Optional.of(user));

        assertThatThrownBy(() -> {

            userService.validateUser(user);

        }).isInstanceOf(EntityNotSavedException.class)
                .hasMessageContaining("A user with that employee ID and role already exists!");




    }

    @DisplayName("Validate user - should throw EntityNotSavedException")
    @Test
    public void invalidEmail_whenValidateUser_thenThrowException(){

        Mockito.when(userRepository.findUserByEmployeeIdAndRole(user.getEmployee().getId(), user.getRole())).thenReturn(Optional.empty());
        Mockito.when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        assertThatThrownBy(() -> {

            userService.validateUser(user);

        }).isInstanceOf(EntityNotSavedException.class)
                .hasMessageContaining("A user with that email already exists!");




    }

}
