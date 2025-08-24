package doktoree.backend.domain;

import com.fasterxml.jackson.annotation.JsonInclude;

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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Classroom {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "class_room_number", nullable = false)
	private String classRoomNumber;
	
	@Enumerated(EnumType.STRING)
	private ClassRoomType classRoomType;
	
	@Column(nullable = false)
	private int capacity;
	
	@Column(name = "number_of_computers", nullable = false)
	private int numberOfComputers;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Classroom that = (Classroom) o;
		return id != null && id.equals(that.id);
	}

	@Override
	public int hashCode() {
		return id != null ? id.hashCode() : 0;
	}
	
}
