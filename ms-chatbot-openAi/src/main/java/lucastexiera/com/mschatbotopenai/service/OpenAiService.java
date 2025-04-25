package lucastexiera.com.mschatbotopenai.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lucastexiera.com.mschatbotopenai.dto.chatbot.OpenAiMessageRequest;
import lucastexiera.com.mschatbotopenai.dto.chatbot.OpenAiMessageResponse;
import lucastexiera.com.mschatbotopenai.dto.chatbot.OpenAiRequestFactory;
import lucastexiera.com.mschatbotopenai.dto.financemonify.CategoryDTO;
import lucastexiera.com.mschatbotopenai.dto.financemonify.NewExpense;
import lucastexiera.com.mschatbotopenai.dto.userwhatsapp.ChatbotMessage;
import lucastexiera.com.mschatbotopenai.dto.userwhatsapp.WhatsappUserMessageResponse;
import lucastexiera.com.mschatbotopenai.infra.openfeign.FinanceClient;
import lucastexiera.com.mschatbotopenai.infra.openfeign.MonifyGatewayClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OpenAiService {

    private static final Logger log = LoggerFactory.getLogger(OpenAiService.class);

    @Autowired
    private RestTemplate restTemplate;

    @Value("${openAi.api.baseURL}")
    private String OPENAI_URL;

    @Autowired
    private MonifyGatewayClient gatewayClient;

    @Autowired
    private ConversationService conversationService;

    @Autowired
    private ChatFunctionHandlerService functionHandlerService;



    public ChatbotMessage sendMessageOpenAi(WhatsappUserMessageResponse userMessage) {
        var userListCategories = gatewayClient.
                findCategoriesByPhoneNumber(userMessage.from()).getBody();

        log.info("userListCategories={}", userListCategories);

        var userConversation = conversationService.saveUserMessage(userMessage.from(), userMessage.message());
        var request = OpenAiRequestFactory.instance(userConversation, userListCategories);

        HttpEntity<OpenAiMessageRequest> requestHttpEntity = new HttpEntity<>(request);

        var openAiResponse = restTemplate.exchange(
                OPENAI_URL,
                HttpMethod.POST,
                requestHttpEntity,
                OpenAiMessageResponse.class
        ).getBody();

        var typeOpenAIMessage = openAiResponse.output().get(0).type();

        // refatorar
        if (typeOpenAIMessage.equals("function_call")) {
            var typeFunctionCall = openAiResponse.output().get(0).name();
            log.info("function type, {}", typeFunctionCall);

            try {
                if (typeFunctionCall.equals("enviar_despesa")) {
                    return functionHandlerService.SaveNewExpense(openAiResponse, userListCategories, userMessage.from());
                } else if (typeFunctionCall.equals("create_category")) {
                    return functionHandlerService.saveNewCategory(openAiResponse, userMessage.from());

                } else if (typeFunctionCall.equals("delete_category")) {
                    return functionHandlerService.deleteCategory(openAiResponse, userMessage.from());
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        var contentMessageChatBot = openAiResponse.output().get(0).content().get(0).text();
        var chatbotMessage = new ChatbotMessage(contentMessageChatBot);

        conversationService.saveAssistantMessage(userMessage.from(), chatbotMessage.message());
        log.info("Normal Conversation={}", chatbotMessage.message());
        return chatbotMessage;

    }



}
