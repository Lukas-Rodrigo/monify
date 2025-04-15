package lucastexiera.com.mswhatsapp.infra.openfeign;


import lucastexiera.com.mswhatsapp.dto.Chatbot.ChatBotRequest;
import lucastexiera.com.mswhatsapp.dto.Chatbot.ChatBotResponse;
import lucastexiera.com.mswhatsapp.dto.Chatbot.OpenAiMessageResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "ms-chatbot-openAi", url =  "http://localhost:8082")
public interface ChatbotClient {

    @PostMapping("v1/chatbot/messages/whatsapp")
    public ChatBotResponse sendoMessageToChatBot(@RequestBody ChatBotRequest message);
}
