package doktoree.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import doktoree.backend.domain.ColloquiumReservation;
import doktoree.backend.ids.ReservationId;

@Repository
public interface ColloquimReservationRepository extends JpaRepository<ColloquiumReservation, ReservationId> {

}
