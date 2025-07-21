package doktoree.backend.domain;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue(value = "OTHER_WORKSHOP")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OtherWorkshopReservation extends Reservation {

	
	private String name;
	
	@OneToMany(mappedBy = "otherWorkshopReservation",
			       cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<WorkshopParticipant> participants;
	 
	
	
}
