package doktoree.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import doktoree.backend.domain.Classroom;

@Repository
public interface ClassroomRepository extends JpaRepository<Classroom, Long> {

}
