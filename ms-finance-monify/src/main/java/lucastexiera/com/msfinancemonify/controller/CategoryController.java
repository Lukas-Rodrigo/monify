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


    @GetMapping
    public ResponseEntity<List<Category>> findAll(Long id) {
        var listCategory =  categoryService.findCategoriesByUserId(id);
        return ResponseEntity.ok(listCategory);
    }

    @PostMapping
    public ResponseEntity<Category> saveCategory(@RequestBody CategoryDTO category) {
        var newCategory = categoryService.save(category);
        return ResponseEntity.ok(newCategory);
    }

}
