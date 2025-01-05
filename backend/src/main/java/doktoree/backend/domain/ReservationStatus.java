package doktoree.backend.domain;

import doktoree.backend.enums.Status;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
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
	private Long id;
	
	@MapsId
	@ManyToOne
	@JoinColumn(name = "id", referencedColumnName = "id")
	private Reservation reservation;
	
	@Enumerated(EnumType.STRING)
	private Status status;
	
	@Column(name = "rejecting_reason")
	private String rejectingReason;
	
	@PrePersist
    @PreUpdate
    public void validateRejectionReason() {
        if (this.status == Status.REJECTED && (rejectingReason == null || rejectingReason.trim().isEmpty())) {
            throw new IllegalArgumentException("Rejection reason must be provided when the status is REJECTED");
        } else if (this.status != Status.REJECTED && rejectingReason != null && !rejectingReason.trim().isEmpty()) {
        	rejectingReason = null;
        }
    }
	
}
