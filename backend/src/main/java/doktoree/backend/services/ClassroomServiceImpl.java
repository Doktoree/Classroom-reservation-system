package doktoree.backend.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import doktoree.backend.domain.Classroom;
import doktoree.backend.dtos.ClassroomDto;
import doktoree.backend.error_response.Response;
import doktoree.backend.exceptions.EntityNotDeletedException;
import doktoree.backend.exceptions.EntityNotExistingException;
import doktoree.backend.exceptions.EntityNotSavedException;
import doktoree.backend.mapper.ClassroomMapper;
import doktoree.backend.repositories.ClassroomRepository;

@Service
public class ClassroomServiceImpl implements ClassroomService {

	@Autowired
	private ClassroomRepository classroomRepository;
	
	@Override
	public Response<ClassroomDto> findClassroomById(Long id) throws EntityNotExistingException {
		
		Optional<Classroom> optionalClassroom = classroomRepository.findById(id);
		Response<ClassroomDto> response = new Response<>();
		
		
		if(optionalClassroom.isPresent()) {
			
			ClassroomDto classroomDto = ClassroomMapper.mapToClassroomDto(optionalClassroom.get());
			response.setDto(classroomDto);
			response.setMessage("Classroom found successfully!");
			return response;
			
		}
			
		
		throw new EntityNotExistingException("There is not classroom with given ID!");
	}

	@Override
	public Response<ClassroomDto> saveClassroom(ClassroomDto dto) throws EntityNotSavedException {
		
		Classroom classroom = ClassroomMapper.mapToClassroom(dto);
		Response<ClassroomDto> response = new Response<>();
		
		try {
			Classroom savedClassroom = classroomRepository.save(classroom);
			response.setDto(ClassroomMapper.mapToClassroomDto(savedClassroom));
			response.setMessage("Classroom successfully saved!");
			return response;
		} catch (Exception e) {
			throw new EntityNotSavedException("Classroom can not be saved!");
		}
		
		
	}

	@Override
	public Response<ClassroomDto> deleteClassroom(Long id) throws EntityNotExistingException, EntityNotDeletedException {

		Optional<Classroom> optionalClassroom = classroomRepository.findById(id);
		
		if(!optionalClassroom.isPresent())
			throw new EntityNotExistingException("There is not classroom with given ID!");
		
		try {
			classroomRepository.deleteById(id);
			Classroom classroom = optionalClassroom.get();
			ClassroomDto dto = ClassroomMapper.mapToClassroomDto(classroom);
			Response<ClassroomDto> response = new Response<>();
			response.setDto(dto);
			response.setMessage("Classroom successfully deleted!");
			return response;
		} catch (Exception e) {
			throw new EntityNotDeletedException("Classroom can not be deleted");
		}
		
	}

	@Override
	public Response<List<ClassroomDto>> getAllClassrooms() {
		
		List<Classroom> classrooms = classroomRepository.findAll();
		
		List<ClassroomDto> classroomDtos =  classrooms.stream().map(ClassroomMapper::mapToClassroomDto).collect(Collectors.toList());
		Response<List<ClassroomDto>> response = new Response<>();
		response.setDto(classroomDtos);
		response.setMessage("All classrooms found successfully!");
		return response;
	}

	@Override
	public Response<ClassroomDto> updateClassroom(ClassroomDto dto) throws EntityNotExistingException, EntityNotSavedException {
			
		Classroom classroom = classroomRepository.findById(dto.id()).orElseThrow(()-> new EntityNotExistingException("There is not classroom with given ID!"));
		
		try {
			classroom.setClassRoomNumber(dto.classroomNumber());
			classroom.setClassRoomType(dto.classRoomType());
			classroom.setCapacity(dto.capacity());
			classroom.setNumberOfComputers(dto.numberOfComputers());
			classroomRepository.save(classroom);
			ClassroomDto classroomDto = ClassroomMapper.mapToClassroomDto(classroom);
			Response<ClassroomDto> response = new Response<>();
			response.setDto(classroomDto);
			response.setMessage("Classroom successfully updated!");
			return response;
			
		} catch (Exception e) {
			throw new EntityNotSavedException("Classroom can not be updated!");
		}
		
	}

}
