package doktoree.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import doktoree.backend.domain.CouncilReservation;
import doktoree.backend.ids.ReservationId;

@Repository
public interface CouncilReservationRepository extends JpaRepository<CouncilReservation, ReservationId> {

}
