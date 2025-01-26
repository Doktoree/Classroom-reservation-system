package doktoree.backend.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import doktoree.backend.domain.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

	public Page<Employee> findAll(Pageable pageable);
	
}
