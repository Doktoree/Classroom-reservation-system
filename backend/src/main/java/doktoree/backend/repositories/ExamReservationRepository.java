package doktoree.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import doktoree.backend.domain.ExamReservation;
import doktoree.backend.ids.ReservationId;

@Repository
public interface ExamReservationRepository extends JpaRepository<ExamReservation, ReservationId> {

}
