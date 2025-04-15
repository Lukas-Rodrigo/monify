package lucastexiera.com.msfinancemonify.controller;

import lucastexiera.com.msfinancemonify.dto.ExpenseDTO;
import lucastexiera.com.msfinancemonify.model.Expense;
import lucastexiera.com.msfinancemonify.service.ExpenseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/api/expense")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    private Logger log = LoggerFactory.getLogger(ExpenseController.class);


    @GetMapping
    public ResponseEntity<List<Expense>> findAll() {
        var listExpense =  expenseService.findAll();
        return ResponseEntity.ok(listExpense);
    }

    @PostMapping()
    public ResponseEntity<Void> saveExpense(@RequestBody ExpenseDTO expense) {
        expenseService.save(expense);
        return ResponseEntity.ok().build();
    }


}
