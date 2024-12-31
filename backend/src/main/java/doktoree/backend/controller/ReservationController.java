package doktoree.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import doktoree.backend.dtos.ReservationDto;
import doktoree.backend.error_response.Response;
import doktoree.backend.services.ReservationServiceImpl;

@RestController
@RequestMapping("/api/reservation")
public class ReservationController {

	
	@Autowired
	private ReservationServiceImpl reservationService;
	
	@GetMapping("{id}")
	public ResponseEntity<Response<ReservationDto>> getReservation(@PathVariable Long id){
		
		return ResponseEntity.ok(reservationService.findReservationById(id));
		
	}
	
	@PostMapping
	public ResponseEntity<Response<ReservationDto>> saveReservation(@RequestBody ReservationDto dto){
		
		
		return ResponseEntity.status(HttpStatus.CREATED).body(reservationService.saveReservation(dto));
		
	}
	
	
	@GetMapping
	public ResponseEntity<Response<List<ReservationDto>>> getAllReservations(){
		
		return ResponseEntity.ok(reservationService.getAllReservations());
		
	}
	
}
