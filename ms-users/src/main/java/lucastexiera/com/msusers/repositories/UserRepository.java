package lucastexiera.com.msusers.repositories;

import lucastexiera.com.msusers.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByPhoneNumber(String phoneNumber);
}
