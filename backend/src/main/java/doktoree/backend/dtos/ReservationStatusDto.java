package doktoree.backend.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;

import doktoree.backend.domain.Reservation;
import doktoree.backend.enums.Status;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReservationStatusDto {

	private Long id;
	
	@NotNull(message = "Reservation can not be null!")
	private ReservationDto reservationDto;
	
	@NotNull(message = "Status can not be null!")
	private Status status;
	
	private String rejectingReason;
	
	
}
