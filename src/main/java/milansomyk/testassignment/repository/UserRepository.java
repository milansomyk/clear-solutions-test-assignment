package milansomyk.testassignment.repository;

import milansomyk.testassignment.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {
    List<User> searchUserByBirthDateBetween(LocalDate from, LocalDate to);
}
