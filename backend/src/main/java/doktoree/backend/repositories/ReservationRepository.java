package doktoree.backend.repositories;



import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import doktoree.backend.domain.Reservation;
import doktoree.backend.domain.User;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long>{

	public Page<Reservation> findByUser(User user, Pageable pageable);
	
	public Page<Reservation> findAll(Pageable pageable);
	
	public List<Reservation> findByUser(User user);
	
}
