package lucastexiera.com.msfinancemonify.repositories;

import lucastexiera.com.msfinancemonify.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseRepository extends JpaRepository<Expense,Long> {
}
