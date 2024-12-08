package doktoree.backend.dtos;

import doktoree.backend.enums.ClassRoomType;

public record ClassroomDto(
		Long id,
		String classroomNumber,
		ClassRoomType classRoomType,
		int capacity,
		int numberOfComputers) {

}
