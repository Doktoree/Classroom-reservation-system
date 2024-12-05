package doktoree.backend.domain;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue(value = "OTHER")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OtherMeetingReservation extends Reservation {

	@Column(name = "short_description")
	private String shortDescription;
	
}
