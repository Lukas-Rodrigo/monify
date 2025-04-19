package lucastexiera.com.mschatbotopenai.infra.openfeign;

import lucastexiera.com.mschatbotopenai.dto.financemonify.CategoryDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "ms-monify-gateway", url = "localhost:8080/proxy")
public interface MonifyGatewayClient {

    @GetMapping("{phoneNumber}")
    public ResponseEntity<List<CategoryDTO>> findCategoriesByPhoneNumber(@PathVariable String phoneNumber);
}
