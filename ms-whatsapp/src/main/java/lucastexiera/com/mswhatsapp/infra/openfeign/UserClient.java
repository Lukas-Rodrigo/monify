package lucastexiera.com.mswhatsapp.infra.openfeign;

import lucastexiera.com.mswhatsapp.dto.Chatbot.ChatBotResponse;
import lucastexiera.com.mswhatsapp.dto.users.CategoryDTO;
import lucastexiera.com.mswhatsapp.dto.users.UserRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "ms-users", url =  "http://localhost:8084")
public interface UserClient {

    @PostMapping("v1/api/user/findUserCategories")
    public ResponseEntity<List<CategoryDTO>> findUserCategories(@RequestBody UserRequest userRequest);
}
