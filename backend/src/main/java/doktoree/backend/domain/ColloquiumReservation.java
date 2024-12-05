package doktoree.backend.domain;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue(value = "COLLOQUIM")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ColloquiumReservation extends Reservation {

	@Column(name = "subject_name")
	private String subjectName; 
	
}
