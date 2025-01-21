package doktoree.backend.dtos;


import com.fasterxml.jackson.annotation.JsonInclude;

import doktoree.backend.domain.Reservation;
import doktoree.backend.domain.User;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReservationNotificationDto {

	private Long id;
	
	@NotNull
	private String message;
	
	@NotNull
	private Reservation reservation;
	
	@NotNull
	private User user;
	
}
