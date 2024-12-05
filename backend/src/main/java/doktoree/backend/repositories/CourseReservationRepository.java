package doktoree.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import doktoree.backend.domain.CourseReservation;
import doktoree.backend.ids.ReservationId;

@Repository
public interface CourseReservationRepository extends JpaRepository<CourseReservation, ReservationId> {

}
