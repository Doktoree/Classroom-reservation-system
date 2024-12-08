package doktoree.backend.domain;

import doktoree.backend.enums.Status;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "reservation_status")
public class ReservationStatus {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Id
	@ManyToOne
	@JoinColumns({
		
		@JoinColumn(name = "user_id", referencedColumnName = "user_id"),
		@JoinColumn(name = "classroom_id", referencedColumnName = "classroom_id")
		
	})
	private Reservation reservation;
	
	@Enumerated(EnumType.STRING)
	private Status status;
	
	@Column(name = "rejecting_reason")
	private String rejectingReason;
	
}
