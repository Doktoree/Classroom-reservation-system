package doktoree.backend.repositories;

import doktoree.backend.enums.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import doktoree.backend.domain.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	public Page<User> findAll(Pageable pageable);

	public Optional<User> findByEmail(String email);

	public Optional<User> findUserByEmployeeIdAndRole(Long employeeId, Role role);
}
