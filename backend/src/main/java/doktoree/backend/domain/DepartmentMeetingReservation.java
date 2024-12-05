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
@DiscriminatorValue(value = "DEPARTMENT_MEETING")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentMeetingReservation extends Reservation {

	@ManyToOne
	@JoinColumn(name = "department_id")
	private Department department;
	
}
