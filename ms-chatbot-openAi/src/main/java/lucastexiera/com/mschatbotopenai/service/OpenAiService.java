package lucastexiera.com.mschatbotopenai.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.annotation.PostConstruct;
import lucastexiera.com.mschatbotopenai.dto.chatbot.OpenAiMessageRequest;
import lucastexiera.com.mschatbotopenai.dto.chatbot.OpenAiMessageResponse;
import lucastexiera.com.mschatbotopenai.dto.financemonify.CategoryDTO;
import lucastexiera.com.mschatbotopenai.dto.userwhatsapp.ChatbotMessage;
import lucastexiera.com.mschatbotopenai.dto.userwhatsapp.WhatsappUserMessageResponse;
import lucastexiera.com.mschatbotopenai.service.strategy.*;
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

import static lucastexiera.com.mschatbotopenai.dto.chatbot.OpenAiRequestFactory.instanceOpenAiMessage;

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
    private ToolHandleService functionHandlerService;

    @Autowired
    private UsersService usersService;

    @Autowired
    private ProcessUserMessageService userMessageService;

    private Map<String, ChatBotFunctionStrategy> mapStrategy;
    private Map<String, TypeMessageStrategy> typeMessageMapStrategy;


    @PostConstruct
    public void init() {
        mapStrategy = Map.of(
            "enviar_despesa",
            new SaveNewExpenseStrategy(functionHandlerService),
            "create_category",
            new CreateCategoryStrategy(functionHandlerService),
            "update_last_expense",
            new UpdateLastExpense(functionHandlerService)
        );

        typeMessageMapStrategy = Map.of(
            "normalMessage",
            new ProcessNormalMessageStrategy(userMessageService),
            "toolMessage",
            new ProcessToolMessageStrategy(userMessageService)
        );


    }

    public ChatbotMessage sendMessageOpenAi(
        WhatsappUserMessageResponse userMessage
    ){
        var userListCategories = usersService.findCategoriesByPhoneNumber(userMessage.from());
        conversationService.saveUserMessage(
            userMessage.from(),
            userMessage.message()
        );
        var request = processRequest(
            userMessage,
            userListCategories
        );
        HttpEntity<OpenAiMessageRequest> requestHttpEntity = new HttpEntity<>(request);
        var openAiResponse = restTemplate
            .exchange(
                OPENAI_URL,
                HttpMethod.POST,
                requestHttpEntity,
                OpenAiMessageResponse.class
            )
            .getBody();

        String typeMessage = checkMessageType(openAiResponse);

        return typeMessageMapStrategy
            .get(typeMessage)
            .handleTypeMessage(
                openAiResponse,
                userMessage,
                userListCategories
            );


    }

    private OpenAiMessageRequest processRequest(
        WhatsappUserMessageResponse userMessage,
        List<CategoryDTO> userListCategories
    ) {

        var userConversation = conversationService.getRecentMessagesByUserPhoneNumber(
            userMessage.from(),
            2
        );

        return instanceOpenAiMessage(
            userConversation,
            userListCategories
        );

    }

    private String checkMessageType(OpenAiMessageResponse openAiResponse) {
        String typeMessage = "normalMessage";
        var typeOpenAIMessage = openAiResponse
            .choices()
            .get(0)
            .message()
            .content();
        if (typeOpenAIMessage == null) {
            typeMessage = "toolMessage";
        }
        return typeMessage;
    }


}

