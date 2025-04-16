package lucastexiera.com.msfinancemonify.controller;

import lombok.extern.slf4j.Slf4j;
import lucastexiera.com.msfinancemonify.dto.CategoryDTO;
import lucastexiera.com.msfinancemonify.dto.UserResponse;
import lucastexiera.com.msfinancemonify.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("v1/api/webhook/users")
@Slf4j
public class UserHookController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping("findCategoriesByUserId")
    public List<CategoryDTO> findCategoriesByUserId(@RequestBody UserResponse userResponse) {
        log.info("Data users: {}", userResponse);
        return categoryService.findCategoriesByUserId(userResponse.userId())
                .stream()
                .map(category -> new CategoryDTO(category.getName()))
                .collect(Collectors.toList());
    }
}
