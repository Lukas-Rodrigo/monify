
package lucastexiera.com.msfinancemonify.service;

import lucastexiera.com.msfinancemonify.dto.CategorySummary;
import lucastexiera.com.msfinancemonify.dto.ExpensesSummaryInPeriodDTO;
import lucastexiera.com.msfinancemonify.dto.ExpenseDTO;
import lucastexiera.com.msfinancemonify.model.Category;
import lucastexiera.com.msfinancemonify.model.Expense;
import lucastexiera.com.msfinancemonify.repositories.CategoryRepository;
import lucastexiera.com.msfinancemonify.repositories.ExpenseRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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


    public List<Expense> findAll(Long userId) {
        return expenseRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public List<Expense> lastSevenDays(Long userId) {

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime sevenDaysAgoStart = now.minusDays(7).with(LocalTime.MIN);
        LocalDateTime todayEnd = now.with(LocalTime.MAX);
        return expenseRepository.findByUserIdAndCreatedAtBetweenOrderByCreatedAtDesc(userId, sevenDaysAgoStart, todayEnd);
    }


    public Expense saveNewExpense(ExpenseDTO dto, Long userId) {
        Category category = categoryRepository.findById(dto.category_id())
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));
        Expense exp = new Expense();
        exp.setDescription(dto.description());
        exp.setAmount(dto.amount());
        exp.setCategory(category);
        exp.setCreatedAt(LocalDateTime.now());
        exp.setUserId(userId);
        return expenseRepository.save(exp);
    }


    public void updateLastExpense(Long userId, ExpenseDTO dto) {
        Expense last = expenseRepository.findTopByUserIdOrderByCreatedAtDesc(userId)
                .orElseThrow(() -> new RuntimeException("Nenhuma despesa encontrada para o usuário"));
        Category category = categoryRepository.findById(dto.category_id())
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));
        last.setDescription(dto.description());
        last.setAmount(dto.amount());
        last.setCategory(category);
        expenseRepository.save(last);
    }

    public ExpensesSummaryInPeriodDTO findExpensesInPeriod(Long userId,
                                                           LocalDate from,
                                                           LocalDate to) {
        LocalDate today = LocalDate.now();
        LocalDate startDate = (from != null ? from : today.minusDays(7));
        LocalDate endDate   = (to   != null ? to   : today);

        LocalDateTime dtFrom = startDate.atStartOfDay();
        LocalDateTime dtTo   = endDate.atTime(LocalTime.MAX);

        List<Expense> list = expenseRepository
                .findByUserIdAndCreatedAtBetweenOrderByCreatedAtDesc(userId, dtFrom, dtTo);

        BigDecimal total = calculateTotal(list);
        CategorySummary top = frequencyCategory(list);

        return new ExpensesSummaryInPeriodDTO(
                top.categoryName(),
                top.totalCategoryExpense(),
                total
        );
    }

    // --- métodos privados de auxílio ---

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
