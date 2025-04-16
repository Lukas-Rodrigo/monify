package lucastexiera.com.msfinancemonify.repositories;

import lucastexiera.com.msfinancemonify.model.Category;
import lucastexiera.com.msfinancemonify.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category,Long> {
    List<Category> findByUserId(Long id);
}
