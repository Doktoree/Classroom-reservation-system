package doktoree.backend.domain;

import doktoree.backend.enums.ClassRoomType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Classroom {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "class_room_number" ,nullable = false)
	private String classRoomNumber;
	
	@Enumerated(EnumType.STRING)
	private ClassRoomType classRoomType;
	
	@Column(nullable = false)
	private int capacity;
	
	@Column(name = "number_of_computers", nullable = false)
	private int numberOfComputers;
	
}
