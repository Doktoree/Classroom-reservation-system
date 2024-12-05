package doktoree.backend.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "workshop_participant")
public class WorkshopParticipant {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(nullable = false)
	private String name;
	
	@Column(nullable = false, name = "last_name")
	private String lastName;
	
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name = "user_id", referencedColumnName = "user_id"),
		@JoinColumn(name = "classroom_id", referencedColumnName = "classroom_id")
	})
	private OtherWorkshopReservation otherWorkshopReservation;
}
