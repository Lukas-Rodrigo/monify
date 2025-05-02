package lucastexiera.com.mschatbotopenai.service;

import jakarta.annotation.PostConstruct;
import lucastexiera.com.mschatbotopenai.dto.chatbot.OpenAiMessageRequest;
import lucastexiera.com.mschatbotopenai.dto.chatbot.OpenAiMessageResponse;
import lucastexiera.com.mschatbotopenai.dto.chatbot.OpenAiRequestFactory;
import lucastexiera.com.mschatbotopenai.dto.financemonify.CategoryDTO;
import lucastexiera.com.mschatbotopenai.dto.userwhatsapp.ChatbotMessage;
import lucastexiera.com.mschatbotopenai.dto.userwhatsapp.WhatsappUserMessageResponse;
import lucastexiera.com.mschatbotopenai.infra.openfeign.FinanceClient;
import lucastexiera.com.mschatbotopenai.infra.openfeign.UsersClient;
import lucastexiera.com.mschatbotopenai.service.strategy.CreateCategoryStrategy;
import lucastexiera.com.mschatbotopenai.service.strategy.SaveNewExpenseStrategy;
import lucastexiera.com.mschatbotopenai.service.strategy.UpdateLastCategory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class OpenAiService {

    private static final Logger log = LoggerFactory.getLogger(OpenAiService.class);

    @Autowired
    private RestTemplate restTemplate;

    @Value("${openAi.api.baseURL}")
    private String OPENAI_URL;


    @Autowired
    private ConversationService conversationService;

    @Autowired
    private ChatFunctionHandlerService functionHandlerService;

    @Autowired
    private UsersService usersService;

    private Map<String, ChatBotFunctionStrategy> mapStrategy;

    @PostConstruct
    public void init() {
        mapStrategy = Map.of(
                "enviar_despesa", new SaveNewExpenseStrategy(functionHandlerService),
                "create_category", new CreateCategoryStrategy(functionHandlerService),
                "update_last_category", new UpdateLastCategory(functionHandlerService)
        );
    }

    public ChatbotMessage sendMessageOpenAi(WhatsappUserMessageResponse userMessage) {
        var userListCategories = usersService.findCategoriesByPhoneNumber(userMessage.from());

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

        if (typeOpenAIMessage.equals("function_call")) {
            var typeFunctionCall = openAiResponse.output().get(0).name();
            log.info("function type, {}", typeFunctionCall);
            try {
                return mapStrategy.get(typeFunctionCall).handle(openAiResponse, userListCategories, userMessage);

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
