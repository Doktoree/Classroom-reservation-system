package doktoree.backend.controller;

import doktoree.backend.dtos.ReservationDto;
import doktoree.backend.enums.Status;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import doktoree.backend.dtos.ReservationStatusDto;
import doktoree.backend.errorresponse.Response;
import doktoree.backend.services.ReservationStatusServiceImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
@RequestMapping("/api/reservation-status/")
@CrossOrigin(origins = "http://localhost:5173")
public class ReservationStatusController {

	
	private final ReservationStatusServiceImpl reservationStatusService;
	
	@Autowired
	public ReservationStatusController(ReservationStatusServiceImpl reservationStatusService) {
		super();
		this.reservationStatusService = reservationStatusService;
	}

	@GetMapping("{id}")
	public ResponseEntity<Response<ReservationStatusDto>> getReservationStatus(
			@PathVariable Long id
	) {
		
		return ResponseEntity.ok(reservationStatusService.findReservationStatusById(id));
		
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping
	public ResponseEntity<Response<ReservationStatusDto>> saveReservationStatus(
			@RequestBody ReservationStatusDto dto
	) {
		
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(reservationStatusService.saveReservationStatus(dto));
		
	}
	
	@GetMapping("user/{id}")
	public ResponseEntity<Response<List<ReservationStatusDto>>> getAllReservationStatusFromUser(
			@PathVariable Long id
	) {
		
		return ResponseEntity.ok(reservationStatusService
				.getAllReservationStatusFromUser(id));
		
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PatchMapping("approve")
	public ResponseEntity<Response<ReservationStatusDto>> approveReservation(
			@RequestBody ReservationStatusDto dto
	) {
		
		return ResponseEntity.ok(reservationStatusService.approveReservation(dto));
		
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PatchMapping("reject")
	public ResponseEntity<Response<ReservationStatusDto>> rejectReservation(
			@RequestBody ReservationStatusDto dto
	) {
		
		return ResponseEntity.ok(reservationStatusService.rejectReservation(dto));
		
	}

	@GetMapping
	public ResponseEntity<Response<List<ReservationStatusDto>>> getAllReservationStatuses(
			@RequestParam(defaultValue = "0") int pageNumber
	) {

		return ResponseEntity.ok(reservationStatusService.getAllReservationStatus(pageNumber));

	}

	@PostMapping("status/")
	public ResponseEntity<Response<List<ReservationStatusDto>>> getAllReservationStatusesByStatus(
			@RequestParam(defaultValue = "0") int pageNumber,
			@RequestBody ReservationStatusDto reservationStatusDto
	) {

		return ResponseEntity.ok(reservationStatusService.getAllReservationStatusByStatus(pageNumber,reservationStatusDto));

	}
	
}
