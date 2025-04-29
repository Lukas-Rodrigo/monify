package lucastexiera.com.mschatbotopenai.infra.openfeign;

import lucastexiera.com.mschatbotopenai.dto.financemonify.CategoryDTO;
import lucastexiera.com.mschatbotopenai.dto.financemonify.ExpenseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "ms-finance-monify", url =  "http://localhost:8083")
public interface FinanceClient {

    @PostMapping("v1/api/expense")
    public ResponseEntity<Void> saveNewExpense(@RequestBody ExpenseDTO newExpense);

    @PutMapping("v1/api/expense/{userId}")
    public ResponseEntity<Void> updateLastExpense(@PathVariable Long userId, @RequestBody ExpenseDTO newCategory);

    @PostMapping("v1/api/category")
    public ResponseEntity<Void> saveNewCategory(@RequestBody CategoryDTO newCategory);

    @DeleteMapping("v1/api/category")
    public ResponseEntity<Void> deleteCategory(@RequestBody CategoryDTO newCategory);


}
