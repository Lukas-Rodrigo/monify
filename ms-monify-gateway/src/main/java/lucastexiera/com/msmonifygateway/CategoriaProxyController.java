package lucastexiera.com.msmonifygateway;

import lucastexiera.com.msmonifygateway.dto.CategoryDTO;
import lucastexiera.com.msmonifygateway.infra.openfeign.FinanceClient;
import lucastexiera.com.msmonifygateway.infra.openfeign.UsersClient;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("{phoneNumber}")
    public List<CategoryDTO> findUserIdByPhoneNumber(@PathVariable String phoneNumber) {
         var userId = usersClient.findUserIdByPhoneNumber(phoneNumber).getBody();
         return financeClient.findCategoriesByUserId(userId).getBody();

    }

}
