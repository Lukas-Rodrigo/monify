package lucastexiera.com.msmonifygateway;

import lucastexiera.com.msmonifygateway.dto.CategoryDTO;
import lucastexiera.com.msmonifygateway.infra.openfeign.FinanceClient;
import lucastexiera.com.msmonifygateway.infra.openfeign.UsersClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/proxy")
public class CategoriaProxyController {

    @Autowired
    private UsersClient usersClient;

    @Autowired
    private FinanceClient financeClient;

    private static final Logger log = LoggerFactory.getLogger(CategoriaProxyController.class);

    @GetMapping("/categories/{phoneNumber}")
    public ResponseEntity<List<CategoryDTO>> findCategoriesByPhoneNumber(@PathVariable String phoneNumber) {
        var userId = usersClient.findUserIdByPhoneNumber(phoneNumber).getBody();
        var userCategories = financeClient.findCategoriesByUserId(userId).getBody();
        return ResponseEntity.ok(userCategories);
    }

    @GetMapping("/user/{phoneNumber}")
    public ResponseEntity<Long> findUserByNumber(@PathVariable String phoneNumber) {
        var userId = usersClient.findUserIdByPhoneNumber(phoneNumber).getBody();
        return ResponseEntity.ok(userId);
    }

}
