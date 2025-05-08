package lucastexiera.com.msfinancemonify.controller;

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


    @GetMapping("/{userId}")
    public ResponseEntity<List<Expense>> findAll(@PathVariable Long userId) {
        List<Expense> list = expenseService.lastSevenDays(userId);
        return ResponseEntity.ok(list);
    }

    @GetMapping("expenses-period/{userId}")
    public ResponseEntity<ExpensesSummaryInPeriodDTO> ExpesesInPeriod(
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
