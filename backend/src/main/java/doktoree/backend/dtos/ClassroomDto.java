package doktoree.backend.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import doktoree.backend.enums.ClassRoomType;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import lombok.*;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClassroomDto {

	@NotNull(message = "Id can't be null!")
	private Long id;

	@NotNull(message = "Classroom number can't be null!")
	@NotEmpty(message = "Classroom number can't be empty!")
	private String classRoomNumber;

	@NotNull(message = "Classroom type can't be null!")
	private ClassRoomType classRoomType;

	@Min(value = 1, message = "Capacity must be greater than 0!")
	private int capacity;

	@Min(value = 1, message = "Number of computers must be grater than 0!")
	private int numberOfComputers;

	@JsonIgnore
	@AssertTrue(message = "Classroom type must be valid value!")
	public boolean isClassroomTypeInScope(){

		for(ClassRoomType t: ClassRoomType.values()){

			if(classRoomType == t)
				return true;

		}

		return false;

	}

}


