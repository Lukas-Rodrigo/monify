package lucastexiera.com.msmonifygateway.infra.openfeign;

import lucastexiera.com.msmonifygateway.dto.CategoryDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "ms-finance-monify", path = "v1/api/category")
public interface FinanceClient {

    @GetMapping("{userId}")
    public ResponseEntity<List<CategoryDTO>> findCategoriesByUserId(@PathVariable Long userId);
}
