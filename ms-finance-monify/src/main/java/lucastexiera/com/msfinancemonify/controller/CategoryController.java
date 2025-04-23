package lucastexiera.com.msfinancemonify.controller;

import lucastexiera.com.msfinancemonify.dto.CategoryDTO;
import lucastexiera.com.msfinancemonify.model.Category;
import lucastexiera.com.msfinancemonify.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/api/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;


    @GetMapping("{id}")
    public ResponseEntity<List<Category>> findAll(@PathVariable Long id) {
        var listCategory =  categoryService.findCategoriesByUserId(id);
        return ResponseEntity.ok(listCategory);
    }

    @PostMapping
    public ResponseEntity<Void> saveNewCategory(@RequestBody CategoryDTO category) {
        categoryService.save(category);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteCategory(@RequestBody CategoryDTO category) {
        categoryService.deleteCategory(category);
        return ResponseEntity.noContent().build();
    }

}
