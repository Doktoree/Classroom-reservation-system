package doktoree.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import doktoree.backend.dtos.ReservationStatusDto;
import doktoree.backend.error_response.Response;
import doktoree.backend.services.ReservationStatusServiceImpl;

@RestController
@RequestMapping("/api/reservation-status")
public class ReservationStatusController {

	
	@Autowired
	private ReservationStatusServiceImpl reservationStatusService;
	
	@GetMapping("{id}")
	public ResponseEntity<Response<ReservationStatusDto>> getReservationStatus(@PathVariable Long id){
		
		return ResponseEntity.ok(reservationStatusService.findReservationStatusById(id));
		
	}
	
	@PostMapping
	public ResponseEntity<Response<ReservationStatusDto>> saveReservationStatus(@RequestBody ReservationStatusDto dto){
		
		return ResponseEntity.status(HttpStatus.CREATED).body(reservationStatusService.saveReservationStatus(dto));
		
	}
	
	@GetMapping("/user/{id}")
	public ResponseEntity<Response<List<ReservationStatusDto>>> getAllReservationStatusFromUser(@PathVariable Long id){
		
		return ResponseEntity.ok(reservationStatusService.getAllReservationStatusFromUser(id));
		
	}
	
	@PatchMapping("/approve")
	public ResponseEntity<Response<ReservationStatusDto>> approveReservation(@RequestBody ReservationStatusDto dto){
		
		return ResponseEntity.ok(reservationStatusService.approveReservation(dto));
		
	}
	
	@PatchMapping("/reject")
	public ResponseEntity<Response<ReservationStatusDto>> rejectReservation(@RequestBody ReservationStatusDto dto){
		
		return ResponseEntity.ok(reservationStatusService.rejectReservation(dto));
		
	}
	
}
