package doktoree.backend.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import doktoree.backend.domain.Reservation;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long>{

	
}
