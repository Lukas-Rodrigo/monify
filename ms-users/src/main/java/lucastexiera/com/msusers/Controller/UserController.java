package lucastexiera.com.msusers.Controller;

import lombok.extern.slf4j.Slf4j;
import lucastexiera.com.msusers.dto.CategoryDTO;
import lucastexiera.com.msusers.dto.UserResponse;
import lucastexiera.com.msusers.model.User;
import lucastexiera.com.msusers.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/api/user")
@Slf4j
public class UserController {


    @Autowired
    private UserService userService;

    @GetMapping("/{phoneNumber}")
    public ResponseEntity<Long> findUserIdByPhoneNumber(@PathVariable String phoneNumber) {
         var userId = userService.findUserIdByPhoneNumber(phoneNumber);
         log.info("userId: {}", userId);
         return ResponseEntity.ok(userId);
    }

    @PostMapping
    public ResponseEntity<User> saveUser(@RequestBody User user) {
        log.info("Saving user {}", user);
        userService.saveUser(user);
        return ResponseEntity.ok().build();
    }


}
