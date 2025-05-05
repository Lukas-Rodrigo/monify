package lucastexiera.com.mschatbotopenai.infra.openfeign;

import lucastexiera.com.mschatbotopenai.dto.financemonify.CategoryDTO;
import lucastexiera.com.mschatbotopenai.dto.financemonify.ExpenseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "ms-finance-monify", path =  "v1/finance")
public interface FinanceClient {

    @PostMapping("/expense/{userId}")
    public ResponseEntity<Void> saveNewExpense(@RequestBody ExpenseDTO newExpense, @PathVariable Long userId);

    @GetMapping("category/{userId}")
    public ResponseEntity<List<CategoryDTO>> findCategoriesByUserId(@PathVariable Long userId);

    @PutMapping("/expense/{userId}")
    public ResponseEntity<Void> updateLastExpense(@PathVariable Long userId, @RequestBody ExpenseDTO newCategory);

    @PostMapping("/category/{userId}")
    public ResponseEntity<Void> saveNewCategory(@RequestBody CategoryDTO newCategory, @PathVariable Long userId);

    @DeleteMapping("/category")
    public ResponseEntity<Void> deleteCategory(@RequestBody CategoryDTO newCategory);


}
