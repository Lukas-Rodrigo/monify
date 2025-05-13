package lucastexiera.com.msfinancemonify.controller;

import lombok.extern.slf4j.Slf4j;
import lucastexiera.com.msfinancemonify.dto.CategoryDTO;
import lucastexiera.com.msfinancemonify.model.Category;
import lucastexiera.com.msfinancemonify.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("v1/finance/category")
@Slf4j
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("{userId}")
    public ResponseEntity<List<CategoryDTO>> findCategoriesByUserId(@PathVariable Long userId) {
        log.info("user id: {}", userId);
        var userCategories = categoryService.findCategoriesByUserId(userId)
                .stream()
                .map(category -> new CategoryDTO(category.getId(), category.getName()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(userCategories);
    }

    @PostMapping("{userId}")
    public ResponseEntity<Void> saveNewCategory(@RequestBody CategoryDTO category, @PathVariable Long userId) {
        categoryService.saveNewCategory(category, userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteCategory(@RequestBody CategoryDTO category) {
        categoryService.deleteCategory(category);
        return ResponseEntity.noContent().build();
    }

}
