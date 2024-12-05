package doktoree.backend.domain;

import doktoree.backend.enums.AcademicRank;
import doktoree.backend.enums.Title;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(nullable = false)
	private String name;
	
	@Column(nullable = false)
	private String lastName;
	
	@Enumerated(EnumType.STRING)
	private AcademicRank academicRank;
	
	@Enumerated(EnumType.STRING)
	private Title title;
	
	@ManyToOne
	@JoinColumn(name = "department_id", nullable = false)
	private Department department;
	
	
	
}
