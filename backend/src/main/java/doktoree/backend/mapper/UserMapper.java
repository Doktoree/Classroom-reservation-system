package doktoree.backend.mapper;

import doktoree.backend.domain.Employee;
import doktoree.backend.domain.User;
import doktoree.backend.dtos.UserDto;

public class UserMapper {

	public static User mapToUser(UserDto dto) {
		
		Employee employee = new Employee();
		employee.setId(dto.employeeId());
		
		return new User(dto.id(), dto.email(), dto.password(), dto.role(), employee);
		
	}
	
	public static UserDto mapToUserDto(User user) {
		
		return new UserDto(user.getId(), user.getEmail(), user.getPassword(), user.getRole(), user.getEmployee().getId());
		
	}
	
}
