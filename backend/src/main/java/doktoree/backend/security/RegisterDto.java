package doktoree.backend.security;

import doktoree.backend.enums.Role;
import lombok.Data;

import java.io.Serializable;

@Data
public class RegisterDto implements Serializable {

    private String email;

    private String password;

    private Role role;

    private Long employeeId;

}
