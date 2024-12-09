package doktoree.backend.dtos;

import doktoree.backend.enums.Role;

public record UserDto(
		Long id,
		String email,
		String password,
		Role role,
		Long employeeId) {

}
