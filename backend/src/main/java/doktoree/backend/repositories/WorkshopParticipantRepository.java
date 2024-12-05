package doktoree.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import doktoree.backend.domain.WorkshopParticipant;

@Repository
public interface WorkshopParticipantRepository extends JpaRepository<WorkshopParticipant, Long> {

}
