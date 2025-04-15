package lucastexiera.com.mschatbotopenai.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lucastexiera.com.mschatbotopenai.dto.OpenAiMessageRequestt;

@RestController
@RequestMapping("v1/test")
public class TestController {

    @GetMapping
    public ResponseEntity<OpenAiMessageRequestt> test(){
        var message = new OpenAiMessageRequestt.Message("user", List.of(OpenAiMessageRequestt.Content.of("ola, tudo bem>")));
        return ResponseEntity.ok(OpenAiMessageRequestt.requestInstance(List.of(message)));
    }
}
