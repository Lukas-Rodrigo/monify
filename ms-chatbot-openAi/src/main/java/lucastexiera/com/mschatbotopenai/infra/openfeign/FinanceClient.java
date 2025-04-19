package lucastexiera.com.mschatbotopenai.infra.openfeign;

import lucastexiera.com.mschatbotopenai.dto.financemonify.NewExpense;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "ms-finance-monify", url =  "http://localhost:8083")
public interface FinanceClient {

    @PostMapping("v1/api/expense")
    public ResponseEntity<Void> saveNewExpense(@RequestBody NewExpense newExpense);

}
