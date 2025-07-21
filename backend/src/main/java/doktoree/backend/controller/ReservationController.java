package doktoree.backend.controller;

import java.util.List;
import doktoree.backend.dtos.ClassroomDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import doktoree.backend.dtos.ReservationDto;
import doktoree.backend.errorresponse.Response;
import doktoree.backend.services.ReservationServiceImpl;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reservation/")
@CrossOrigin(origins = "http://localhost:5173")
public class ReservationController {

	private final ReservationServiceImpl reservationService;
	
	@Autowired
	public ReservationController(ReservationServiceImpl reservationService) {
		this.reservationService = reservationService;
	}

	@GetMapping("{id}")
	public ResponseEntity<Response<ReservationDto>> getReservation(@PathVariable Long id) {
		
		return ResponseEntity.ok(reservationService.findReservationById(id));
		
	}
	
	@PostMapping
	public ResponseEntity<Response<ReservationDto>> saveReservation(
			@RequestBody ReservationDto dto
	) {
		
		
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(reservationService.saveReservation(dto));
		
	}
	
	
	@GetMapping
	public ResponseEntity<Response<List<ReservationDto>>> getAllReservations(
			@RequestParam(defaultValue = "0") int pageNumber
	) {
		
		return ResponseEntity.ok(reservationService.getAllReservations(pageNumber));
		
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PatchMapping
	public ResponseEntity<Response<ReservationDto>> updateReservation(
			@RequestBody ReservationDto dto
	) {
		
		return ResponseEntity.ok(reservationService.updateReservation(dto));
		
	}
	
	@GetMapping("user/{id}")
	public ResponseEntity<Response<List<ReservationDto>>> getAllReservationsFromUser(
			@PathVariable Long id, @RequestParam(defaultValue = "0") int pageNumber
	) {
		
		return ResponseEntity.ok(reservationService
				.getAllReservationsFromUser(id, pageNumber));
		
	}

	@PostMapping("availableClassrooms")
	public ResponseEntity<Response<List<ClassroomDto>>> getAllAvailableClassrooms(
			@RequestBody ReservationDto dto
	) {

		return ResponseEntity.ok(reservationService.getAllAvailableClassrooms(dto));

	}
	
}
