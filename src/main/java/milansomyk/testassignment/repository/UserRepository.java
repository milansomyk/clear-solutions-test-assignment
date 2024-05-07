package milansomyk.testassignment.repository;

import milansomyk.testassignment.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    @Query(nativeQuery = true, value = "SELECT * FROM user WHERE birth_date BETWEEN :from AND :to LIMIT :limit OFFSET :offset")
    List<User> searchUserByBirthDateRange(LocalDate from, LocalDate to, Integer limit, Integer offset);
    Integer countAllByBirthDateBetween(LocalDate from, LocalDate to);
}
