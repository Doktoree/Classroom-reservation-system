package doktoree.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import doktoree.backend.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}
