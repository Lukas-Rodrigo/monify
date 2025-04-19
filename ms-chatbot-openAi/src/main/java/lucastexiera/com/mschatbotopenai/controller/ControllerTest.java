package lucastexiera.com.mschatbotopenai.controller;

import lucastexiera.com.mschatbotopenai.dto.chatbot.OpenAiMessageRequestt;
import lucastexiera.com.mschatbotopenai.dto.chatbot.OpenAiRequestFactory;
import lucastexiera.com.mschatbotopenai.dto.financemonify.CategoryDTO;
import lucastexiera.com.mschatbotopenai.model.ChatMessage;
import lucastexiera.com.mschatbotopenai.model.Conversation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/test")
public class ControllerTest {

    @GetMapping("/conversation")
    public Conversation retornConversationObject() {
        // Criar mensagens mockadas
        ChatMessage msg1 = new ChatMessage();
        msg1.setId(1L);
        msg1.setSender("user");
        msg1.setMessage("Gastei 50 reais em alimentação");
        msg1.setTimestamp(LocalDateTime.now().minusMinutes(10));

        ChatMessage msg2 = new ChatMessage();
        msg2.setId(2L);
        msg2.setSender("assistant");
        msg2.setMessage("Certo! Registrei uma despesa de R$50,00 na categoria alimentação.");
        msg2.setTimestamp(LocalDateTime.now().minusMinutes(9));

        // Criar conversa mockada
        Conversation conversation = new Conversation();
        conversation.setId(1L);
        conversation.setUserPhoneNumber("11999999999");
        conversation.setMessages(List.of(msg1, msg2));

        return conversation;
    }

    @GetMapping
    public OpenAiMessageRequestt retornOpenAiMessageObject() {

        // Criar mensagens mockadas
        ChatMessage msg1 = new ChatMessage();
        msg1.setId(1L);
        msg1.setSender("user");
        msg1.setMessage("Gastei 50 reais em alimentação");
        msg1.setTimestamp(LocalDateTime.now().minusMinutes(10));

        ChatMessage msg2 = new ChatMessage();
        msg2.setId(2L);
        msg2.setSender("assistant");
        msg2.setMessage("Certo! Registrei uma despesa de R$50,00 na categoria alimentação.");
        msg2.setTimestamp(LocalDateTime.now().minusMinutes(9));

        // Criar conversa mockada
        Conversation conversation = new Conversation();
        conversation.setId(1L);
        conversation.setUserPhoneNumber("11999999999");
        conversation.setMessages(List.of(msg1, msg2));




        List<CategoryDTO> categoryDTOList = new ArrayList<>();
        categoryDTOList.add(new CategoryDTO(1L, "Alimentacao"));
        categoryDTOList.add(new CategoryDTO(2L, "cartao de credito"));
        return OpenAiRequestFactory.instance(conversation, categoryDTOList);
    }
}

