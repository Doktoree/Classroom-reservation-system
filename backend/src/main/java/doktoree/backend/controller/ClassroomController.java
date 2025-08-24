package doktoree.backend.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import doktoree.backend.errorresponse.Response;
import doktoree.backend.services.ClassroomServiceImpl;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import doktoree.backend.dtos.ClassroomDto;

@RestController
@RequestMapping("/api/classroom/")
@CrossOrigin(origins = "http://localhost:5173")
public class ClassroomController {

	private final ClassroomServiceImpl classroomService;

	@Autowired
	public ClassroomController(ClassroomServiceImpl classroomService) {
		this.classroomService = classroomService;
	}

	@GetMapping("{id}")
	public ResponseEntity<Response<ClassroomDto>> findClassroomById(@PathVariable  Long id) {

		return ResponseEntity.ok(classroomService.findClassroomById(id));
		
		
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping
	public ResponseEntity<Response<ClassroomDto>> saveClassroom(@RequestBody ClassroomDto dto) {
		
		
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(classroomService.saveClassroom(dto));
	}

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("{id}")
	public ResponseEntity<Response<ClassroomDto>> deleteClassroom(@PathVariable Long id) {
		
		return ResponseEntity.ok(classroomService.deleteClassroom(id));
		
	}
	
	@GetMapping("all")
	public ResponseEntity<Response<List<ClassroomDto>>> getAllClassrooms() {
		
		return ResponseEntity.ok(classroomService.getAllClassrooms());
		
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PatchMapping
	public ResponseEntity<Response<ClassroomDto>> updateClassroom(
			@RequestBody ClassroomDto dto
	) {
		
		return ResponseEntity.ok(classroomService.updateClassroom(dto));
		
	}
}
