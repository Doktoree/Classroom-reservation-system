package doktoree.backend.mapper;

import doktoree.backend.domain.Classroom;
import doktoree.backend.dtos.ClassroomDto;

public class ClassroomMapper {

	public static Classroom mapToClassroom(ClassroomDto dto) {
		
		
		return new Classroom(dto.id(),
				dto.classroomNumber(),
				dto.classRoomType(),
				dto.capacity(),
				dto.numberOfComputers());
		
	}
	
	public static ClassroomDto mapToClassroomDto(Classroom classroom) {
		
		return new ClassroomDto(
				classroom.getId(), 
				classroom.getClassRoomNumber(),
				classroom.getClassRoomType(),
				classroom.getCapacity(), 
				classroom.getNumberOfComputers());
		
	}
	
}
