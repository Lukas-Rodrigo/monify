package lucastexiera.com.msusers.infra.feign;

import lucastexiera.com.msusers.dto.CategoryDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "ms-finance-monify", path =  "v1/finance")
public interface FinanceClient {

    @PostMapping("/category/{userId}")
    public ResponseEntity<Void> saveNewCategory(@RequestBody CategoryDTO categoryDTO, @PathVariable Long userId);
}
