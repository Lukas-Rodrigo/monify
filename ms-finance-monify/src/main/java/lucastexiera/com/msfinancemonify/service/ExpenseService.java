
package lucastexiera.com.msfinancemonify.service;

import lucastexiera.com.msfinancemonify.dto.CategoryPercentageDTO;
import lucastexiera.com.msfinancemonify.dto.CategorySummary;
import lucastexiera.com.msfinancemonify.dto.ExpensesSummaryInPeriodDTO;
import lucastexiera.com.msfinancemonify.dto.ExpenseDTO;
import lucastexiera.com.msfinancemonify.model.Category;
import lucastexiera.com.msfinancemonify.model.Expense;
import lucastexiera.com.msfinancemonify.repositories.CategoryRepository;
import lucastexiera.com.msfinancemonify.repositories.ExpenseRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final CategoryRepository categoryRepository;

    public ExpenseService(ExpenseRepository expenseRepository,
                          CategoryRepository categoryRepository) {
        this.expenseRepository = expenseRepository;
        this.categoryRepository = categoryRepository;
    }

    public void saveNewExpense(ExpenseDTO dto, Long userId) {
        Category category = categoryRepository.findById(dto.category_id())
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));
        Expense exp = new Expense();
        exp.setDescription(dto.description());
        exp.setAmount(dto.amount());
        exp.setCategory(category);
        exp.setCreatedAt(LocalDateTime.now());
        exp.setUserId(userId);
        expenseRepository.save(exp);
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

    public ExpensesSummaryInPeriodDTO summaryExpensesInPeriod(Long userId, LocalDate from, LocalDate to) {
        var expensesInPeriod = findExpensesInPeriod(userId, from, to);

        BigDecimal total = calculateTotal(expensesInPeriod);
        CategorySummary top = frequencyCategory(expensesInPeriod);

        return new ExpensesSummaryInPeriodDTO(
                top.categoryName(),
                top.totalCategoryExpense(),
                total
        );
    }

    public List<CategoryPercentageDTO> getCategoryPercentages(
            Long userId, LocalDate from, LocalDate to
    ) {
        var expensesInPeriod = findExpensesInPeriod(userId, from, to);

        BigDecimal total = calculateTotal(expensesInPeriod);

        if (total.compareTo(BigDecimal.ZERO) == 0) {
            return Collections.emptyList();
        }

        var sumByCat = expensesInPeriod.stream()
                .collect(Collectors.groupingBy(
                        expense -> expense.getCategory().getName(),
                        Collectors.reducing(BigDecimal.ZERO, Expense::getAmount, BigDecimal::add)
                ));
        
        return sumByCat.entrySet().stream()
                .map(entry -> {
                    BigDecimal categoryPercentage = entry.getValue()
                            .multiply(BigDecimal.valueOf(100))
                            .divide(total, 2, RoundingMode.HALF_UP);
                    return new CategoryPercentageDTO(entry.getKey(), categoryPercentage);
                })
                .toList();
    }

    public List<Expense> findExpensesInPeriod(Long userId, LocalDate from, LocalDate to) {
        LocalDate today = LocalDate.now();
        LocalDate start = (from != null ? from : today.minusDays(7));
        LocalDate end   = (to   != null ? to   : today);

        LocalDateTime dtFrom = start.atStartOfDay();
        LocalDateTime dtTo   = end.atTime(LocalTime.MAX);

        return expenseRepository
                .findByUserIdAndCreatedAtBetweenOrderByCreatedAtDesc(userId, dtFrom, dtTo);
    }


    private BigDecimal calculateTotal(List<Expense> expenses) {
        return expenses.stream()
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }


    private CategorySummary frequencyCategory(List<Expense> expenses) {
        Map<String, BigDecimal> sumByCat = expenses.stream()
                .collect(Collectors.groupingBy(
                        e -> e.getCategory().getName(),
                        Collectors.reducing(BigDecimal.ZERO, Expense::getAmount, BigDecimal::add)
                ));

        if (sumByCat.isEmpty()) {
            return new CategorySummary("Nenhuma categoria no período", BigDecimal.ZERO);
        }

        var topEntry = Collections.max(
                sumByCat.entrySet(),
                Map.Entry.comparingByValue()
        );

        return new CategorySummary(topEntry.getKey(), topEntry.getValue());
    }
}
