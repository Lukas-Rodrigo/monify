package lucastexiera.com.msfinancemonify.service;

import lucastexiera.com.msfinancemonify.dto.CategoryDTO;
import lucastexiera.com.msfinancemonify.model.Category;
import lucastexiera.com.msfinancemonify.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;


    public List<Category> findCategoriesByUserId(Long userId) {
        return categoryRepository.findByUserId(userId);
    }

    public Category save(CategoryDTO category) {
        var newCategory = createNewCategory(category);
        return categoryRepository.save(newCategory);
    }

    private Category createNewCategory(CategoryDTO category) {
        return new  Category(
                 null,
                category.name(),
                1L
        );
    }

    public void deleteCategory(CategoryDTO category) {
        var categoryId = categoryRepository.findById(category.category_id()).orElseThrow();
        categoryRepository.deleteById(categoryId.getId());
    }
}
