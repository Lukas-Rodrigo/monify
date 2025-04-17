package lucastexiera.com.msfinancemonify.controller;

import lombok.extern.slf4j.Slf4j;
import lucastexiera.com.msfinancemonify.dto.CategoryDTO;
import lucastexiera.com.msfinancemonify.dto.UserResponse;
import lucastexiera.com.msfinancemonify.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("v1/api/webhook/users")
@Slf4j
public class UserHookController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/{userId}")
    public List<CategoryDTO> findCategoriesByUserId(@PathVariable Long userId) {
        log.info("user id: {}", userId);
        return categoryService.findCategoriesByUserId(userId)
                .stream()
                .map(category -> new CategoryDTO(category.getName()))
                .collect(Collectors.toList());
    }
}
