package doktoree.backend.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import doktoree.backend.domain.ReservationNotification;

@Repository
public interface ReservationNotificationRepository extends JpaRepository<ReservationNotification, Long> {

	
	
}
