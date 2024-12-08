package doktoree.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import doktoree.backend.domain.Department;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

}
