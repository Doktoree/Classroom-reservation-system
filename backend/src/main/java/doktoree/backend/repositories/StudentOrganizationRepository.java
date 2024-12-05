package doktoree.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import doktoree.backend.domain.StudentOrganization;

@Repository
public interface StudentOrganizationRepository extends JpaRepository<StudentOrganization, Long>{

}
