package lucastexiera.com.mschatbotopenai.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lucastexiera.com.mschatbotopenai.dto.chatbot.OpenAiMessageRequestt;
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
    private FinanceClient financeClient;

    @Autowired
    private MonifyGatewayClient gatewayClient;

    @Autowired
    private ConversationService conversationService;

    private final ObjectMapper objectMapper = new ObjectMapper();


    public ChatbotMessage sendMessageOpenAi(WhatsappUserMessageResponse userMessage) {

        var userListCategories = gatewayClient.
                findCategoriesByPhoneNumber(userMessage.from()).getBody();

        var userConversation = conversationService.saveUserMessage(userMessage.from(), userMessage.message());
        var request = OpenAiRequestFactory.instance(userConversation, userListCategories);

        HttpEntity<OpenAiMessageRequestt> requestHttpEntity = new HttpEntity<>(request);

        var OpenAiResponse = restTemplate.exchange(
                OPENAI_URL,
                HttpMethod.POST,
                requestHttpEntity,
                OpenAiMessageResponse.class
        ).getBody();

        var typeOpenAIMessage = OpenAiResponse.output().get(0).type();
        ChatbotMessage chatbotMessage;

        if (typeOpenAIMessage.equals("function_call") ) {
            try {
                return SaveNewExpense(OpenAiResponse, userListCategories, userMessage.from());
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Erro ao processar dados da despesa", e);
            }
        } else {
            var contentMessageChatBot = OpenAiResponse.output().get(0).content().get(0).text();
            chatbotMessage = new ChatbotMessage(contentMessageChatBot);
        }

        conversationService.saveAssistantMessage(userMessage.from(), chatbotMessage.message());
        return chatbotMessage;

    }

    private ChatbotMessage SaveNewExpense(OpenAiMessageResponse OpenAiResponse, List<CategoryDTO> userListCategories, String from) throws JsonProcessingException {
        var expenseToBeSavedJson = OpenAiResponse.output().get(0).arguments();
        ChatbotMessage chatbotMessage;

        log.info("Despesa a ser salva: {}", expenseToBeSavedJson);

        var expenseToBeSaved = objectMapper.readValue(expenseToBeSavedJson, NewExpense.class);

        boolean categoriaValida = userListCategories.stream()
                .anyMatch(c -> c.id().equals(expenseToBeSaved.category_id()));


        log.info("Categoria valida: {}", categoriaValida);

        if (!categoriaValida) {
            String categoriasDisponiveis = userListCategories.stream()
                    .map(c -> "- " + c.name())
                    .collect(Collectors.joining("\n"));

            chatbotMessage = new ChatbotMessage("""
                        A categoria que você informou não é válida. 🧐
                        Por favor, escolha uma das categorias disponíveis:

                        %s
                        """.formatted(categoriasDisponiveis));
            conversationService.saveAssistantMessage(from, chatbotMessage.message());
            return chatbotMessage;
        }
        financeClient.saveNewExpense(expenseToBeSaved);
        chatbotMessage = new ChatbotMessage("Prontinho! Sua despesa foi salva. Quer cadastrar mais alguma?");
        conversationService.saveAssistantMessage(from, chatbotMessage.message());

        return chatbotMessage;
    }




}
