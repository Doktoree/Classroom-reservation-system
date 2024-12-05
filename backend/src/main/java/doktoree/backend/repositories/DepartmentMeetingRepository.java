package doktoree.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import doktoree.backend.domain.DepartmentMeetingReservation;
import doktoree.backend.ids.ReservationId;

@Repository
public interface DepartmentMeetingRepository extends JpaRepository<DepartmentMeetingReservation, ReservationId> {

}
