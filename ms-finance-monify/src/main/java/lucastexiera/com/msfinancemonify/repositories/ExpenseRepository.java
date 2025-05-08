package lucastexiera.com.msfinancemonify.repositories;

import lucastexiera.com.msfinancemonify.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ExpenseRepository extends JpaRepository<Expense,Long> {
    List<Expense> findAllByUserId(Long userId);

    Optional<Expense> findTopByUserIdOrderByCreatedAtDesc(Long userId);

    List<Expense> findByUserIdOrderByCreatedAtDesc(Long userId);

    List<Expense> findByUserIdAndCreatedAtBetweenOrderByCreatedAtDesc(
            Long userId,
            LocalDateTime from,
            LocalDateTime to
    );
}
