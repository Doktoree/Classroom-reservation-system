package doktoree.backend.services;

import doktoree.backend.dtos.ClassroomDto;
import doktoree.backend.error_response.Response;
import doktoree.backend.exceptions.EntityNotDeletedException;
import doktoree.backend.exceptions.EntityNotExistingException;
import doktoree.backend.exceptions.EntityNotSavedException;
import java.util.*;

public interface ClassroomService {

	public Response<ClassroomDto> findClassroomById(Long id) throws EntityNotExistingException;
	
	public Response<ClassroomDto> saveClassroom(ClassroomDto dto) throws EntityNotSavedException;
	
	public Response<ClassroomDto> deleteClassroom(Long id) throws EntityNotExistingException, EntityNotDeletedException;
	
	public Response<List<ClassroomDto>> getAllClassrooms();
	
	public Response<ClassroomDto> updateClassroom(ClassroomDto dto) throws EntityNotExistingException, EntityNotSavedException; 
}
