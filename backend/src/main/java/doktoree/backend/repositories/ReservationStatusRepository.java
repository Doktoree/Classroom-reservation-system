package doktoree.backend.repositories;


import doktoree.backend.domain.Reservation;
import doktoree.backend.enums.Status;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import doktoree.backend.domain.ReservationStatus;

@Repository
public interface ReservationStatusRepository extends JpaRepository<ReservationStatus, Long> {

  public Page<ReservationStatus> findAll(Pageable pageable);

  Page<ReservationStatus> findByStatusOrderByIdDesc(Status status, Pageable pageable);

}
