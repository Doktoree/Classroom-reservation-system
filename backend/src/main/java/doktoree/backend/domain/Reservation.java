package doktoree.backend.domain;

import java.time.LocalDate;
import java.time.LocalTime;

import doktoree.backend.ids.ReservationId;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "reservation_purpose", discriminatorType = DiscriminatorType.STRING)
public class Reservation {

	@EmbeddedId
	private ReservationId id;
	
	@Column(nullable = false)
	private LocalDate date;
	
	@Column(nullable = false, name = "start_time")
	private LocalTime startTime;
	
	@Column(nullable = false, name = "end_time")
	private LocalTime endTime;
	
	
	
}
