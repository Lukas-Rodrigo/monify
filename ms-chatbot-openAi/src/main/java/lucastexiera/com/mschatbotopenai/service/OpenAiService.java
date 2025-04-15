package lucastexiera.com.mschatbotopenai.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lucastexiera.com.mschatbotopenai.dto.OpenAiMessageRequest;
import lucastexiera.com.mschatbotopenai.dto.OpenAiMessageRequestt;
import lucastexiera.com.mschatbotopenai.dto.OpenAiMessageResponse;
import lucastexiera.com.mschatbotopenai.dto.financemonify.NewExpense;
import lucastexiera.com.mschatbotopenai.dto.userwhatsapp.ChatbotMessage;
import lucastexiera.com.mschatbotopenai.dto.userwhatsapp.WhatsappUserMessageResponse;
import lucastexiera.com.mschatbotopenai.infra.openfeign.FinanceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class OpenAiService {

    private static final Logger log = LoggerFactory.getLogger(OpenAiService.class);
    @Autowired
    private RestTemplate restTemplate;

    @Value("${openAi.api.baseURL}")
    private String OPENAI_URL;

    @Autowired
    private FinanceClient financeClient;


    public ChatbotMessage sendMessageOpenAi(WhatsappUserMessageResponse userMessage) {

        var promptUser = new OpenAiMessageRequestt.Message(
                "user",
                List.of(OpenAiMessageRequestt.Content.of(userMessage.message()))
        );
        var request = OpenAiMessageRequestt.requestInstance(List.of(promptUser));
        HttpEntity<OpenAiMessageRequestt> requestHttpEntity = new HttpEntity<>(request);
        ResponseEntity<OpenAiMessageResponse> response = restTemplate.exchange(
                OPENAI_URL,
                HttpMethod.POST,
                requestHttpEntity,
                OpenAiMessageResponse.class
        );


        var messageChatbot = response.getBody().output().get(0);

        if (messageChatbot.type().equals("message")) {
            return new ChatbotMessage(messageChatbot.content().get(0).text());
        } else {
            log.info("Despesa a ser salva: {}", messageChatbot.arguments());
            var userExpense = messageChatbot.arguments();
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                var newExpense = objectMapper.readValue(userExpense, NewExpense.class);
                financeClient.saveNewExpense(newExpense);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }



           return new ChatbotMessage("Prontinho! Sua despesa foi salva. Quer cadastrar mais alguma?");
        }

    }


}
