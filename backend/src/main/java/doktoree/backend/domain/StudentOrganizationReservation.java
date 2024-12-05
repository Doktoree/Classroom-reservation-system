package doktoree.backend.domain;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue(value = "STUDENT_ORGANIZATION")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentOrganizationReservation extends Reservation{

	@ManyToOne
	@JoinColumn(name = "student_organization_id")
	private StudentOrganization studentOrganization;
	
}
