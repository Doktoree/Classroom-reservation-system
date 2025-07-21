package doktoree.backend.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import doktoree.backend.enums.Role;
import doktoree.backend.validator.EnumHandler;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class UserDto {

	@NotNull(message = "Id can't be null!")
	private Long id;

	@NotNull(message = "Email can't be null!")
	@NotEmpty(message = "Email can't be empty!")
	private String email;

	@NotNull(message = "Password can't be null!")
	@NotEmpty(message = "Password can't be empty!")
	private String password;

	@NotNull(message = "Role can't be null!")
	private Role role;

	@NotNull(message = "Employee id can't be null!")
	private Long employeeId;

	@AssertTrue(message = "Role must be valid value!")
	@JsonIgnore()
	public boolean isRoleInScope() {

		EnumHandler<Role> enumHandler = new EnumHandler<>(Role.class);
		return enumHandler.isValid(role);

	}

}
