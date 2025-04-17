package lucastexiera.com.msusers.infra.openfeign;

import lucastexiera.com.msusers.dto.CategoryDTO;
import lucastexiera.com.msusers.dto.UserRequest;
import lucastexiera.com.msusers.dto.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "ms-finance-monify", url = "localhost:8083")
public interface FinanceMonifyClient {

    @PostMapping("/v1/api/webhook/users/findCategoriesByUserId")
    public ResponseEntity<List<CategoryDTO>> sendUserId(@RequestBody UserRequest userRequest);
}
