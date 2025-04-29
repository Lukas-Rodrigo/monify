package lucastexiera.com.msfinancemonify.repositories;

import lucastexiera.com.msfinancemonify.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense,Long> {
    List<Expense> findAllByUserId(Long userId);
}
