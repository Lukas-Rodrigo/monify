package lucastexiera.com.msfinancemonify.service;


import lucastexiera.com.msfinancemonify.dto.ExpenseDTO;
import lucastexiera.com.msfinancemonify.model.Category;
import lucastexiera.com.msfinancemonify.model.Expense;
import lucastexiera.com.msfinancemonify.repositories.CategoryRepository;
import lucastexiera.com.msfinancemonify.repositories.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;


    @Autowired
    private CategoryRepository categoryRepository;

    public List<Expense> findAll(Long userId) {
        return expenseRepository.findAllByUserId(userId);
    }

    public Expense saveNewExpense(ExpenseDTO expense, Long userId) {
        Category category = categoryRepository.findById(expense.category_id())
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));

        var newExpense = CreateNewExpense(expense, category, userId);
        return expenseRepository.save(newExpense);
    }

    public void updateLastExpense(Long userId, ExpenseDTO dto) {
        Expense lastExpense = expenseRepository.findTopByUserIdOrderByCreatedAtDesc(userId)
                .orElseThrow(() -> new RuntimeException("Nenhuma despesa encontrada para o usuário"));

        Category category = categoryRepository.findById(dto.category_id())
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));

        lastExpense.setDescription(dto.description());
        lastExpense.setAmount(dto.amount());
        lastExpense.setCategory(category);

        expenseRepository.save(lastExpense);
    }



    private Expense CreateNewExpense(ExpenseDTO newExpense, Category category, Long UserId) {
        return new Expense(null,
                newExpense.description(),
                newExpense.amount(),
                category,
                LocalDateTime.now(),
                UserId
        );
    }
}
