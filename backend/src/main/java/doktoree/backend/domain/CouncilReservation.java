package doktoree.backend.domain;

import doktoree.backend.enums.CouncilType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue(value = "COUNCIL")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CouncilReservation extends Reservation {

	@Enumerated(EnumType.STRING)
	@Column(name = "council_type")
	private CouncilType councilType;
	
}
