package doktoree.backend.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import doktoree.backend.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "`user`")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private String email;
	
	@JsonIgnore
	@Column(nullable = false)
	private String password;
	
	@JsonIgnore
	@Enumerated(EnumType.STRING)
	private Role role;
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "employee_id", nullable = false)
	private Employee employee;
	
}
