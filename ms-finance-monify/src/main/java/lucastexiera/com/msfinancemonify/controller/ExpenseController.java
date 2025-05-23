package lucastexiera.com.msfinancemonify.controller;

import lucastexiera.com.msfinancemonify.dto.CategoryPercentageDTO;
import lucastexiera.com.msfinancemonify.dto.ExpenseDTO;
import lucastexiera.com.msfinancemonify.dto.ExpensesSummaryInPeriodDTO;
import lucastexiera.com.msfinancemonify.model.Expense;
import lucastexiera.com.msfinancemonify.service.ExpenseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("v1/finance/expense")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    private Logger log = LoggerFactory.getLogger(ExpenseController.class);


    @GetMapping("summary-period/{userId}")
    public ResponseEntity<ExpensesSummaryInPeriodDTO> summaryExpensesInPeriod(
            @PathVariable Long userId,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate from,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate to
    ) {
        var expensesInPeriod = expenseService.summaryExpensesInPeriod(userId, from, to);
        return ResponseEntity.ok(expensesInPeriod);
    }

  @GetMapping("expenses-period/{userId}")
  public ResponseEntity<List<Expense>> ExpensesInPeriod(
          @PathVariable Long userId,
          @RequestParam(required = false)
          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
          LocalDate from,
          @RequestParam(required = false)
          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
          LocalDate to
  ) {
    var expensesInPeriod = expenseService.findExpensesInPeriod(userId, from, to);
    return ResponseEntity.ok(expensesInPeriod);
  }

    @GetMapping("categories-percentages/{userId}")
    public ResponseEntity<List<CategoryPercentageDTO>>getCategoryPercentages(
            @PathVariable Long userId,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate from,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate to
    ) {
        var listCategories = expenseService.getCategoryPercentages(userId, from, to);
        return ResponseEntity.ok(listCategories);
    }

    @PostMapping("{userId}")
    public ResponseEntity<Void> saveExpense(@RequestBody ExpenseDTO expense , @PathVariable Long userId) {
        expenseService.saveNewExpense(expense, userId);
        return ResponseEntity.ok().build();
    }
    @PutMapping("{userId}")
    public ResponseEntity<Void> updateLastExpense(@PathVariable Long userId, @RequestBody ExpenseDTO newCategory) {
        expenseService.updateLastExpense(userId, newCategory);
        return ResponseEntity.ok().build();
    }
}
