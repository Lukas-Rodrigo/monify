package lucastexiera.com.msmonifygateway.infra.openfeign;

import lucastexiera.com.msmonifygateway.dto.CategoryDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "ms-users", path = "/v1/users")
public interface UsersClient {

    @GetMapping("{phoneNumber}")
    public ResponseEntity<Long> findUserIdByPhoneNumber(@PathVariable String phoneNumber);
}
