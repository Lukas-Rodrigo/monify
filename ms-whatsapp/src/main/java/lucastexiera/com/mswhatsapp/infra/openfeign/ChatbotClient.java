package lucastexiera.com.mswhatsapp.infra.openfeign;


import lucastexiera.com.mswhatsapp.dto.Chatbot.ChatBotRequest;
import lucastexiera.com.mswhatsapp.dto.Chatbot.ChatBotResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "ms-chatbot-openAi", path =  "v1/chatbot/message")
public interface ChatbotClient {


    @PostMapping("/whatsapp")
    public ChatBotResponse sendoMessageToChatBot(@RequestBody ChatBotRequest message);

    
}
