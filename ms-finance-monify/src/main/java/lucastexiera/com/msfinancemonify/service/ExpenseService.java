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

    public Expense save(ExpenseDTO expense) {
        Category category = categoryRepository.findById(expense.category_id())
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));

        var newExpense = CreateNewExpense(expense, category);
        return expenseRepository.save(newExpense);
    }

    private Expense CreateNewExpense(ExpenseDTO newExpense, Category category) {
        return new Expense(null,
                newExpense.description(),
                newExpense.amount(),
                category,
                LocalDateTime.now(),
                Long.valueOf(1)
        );
    }
}
