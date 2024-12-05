package doktoree.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import doktoree.backend.domain.OtherWorkshopReservation;
import doktoree.backend.ids.ReservationId;

@Repository
public interface OtherWorkshopReservationRepository extends JpaRepository<OtherWorkshopReservation, ReservationId>{

}
