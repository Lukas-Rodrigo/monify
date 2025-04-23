package lucastexiera.com.mschatbotopenai.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lucastexiera.com.mschatbotopenai.dto.chatbot.OpenAiMessageResponse;
import lucastexiera.com.mschatbotopenai.dto.financemonify.CategoryDTO;
import lucastexiera.com.mschatbotopenai.dto.financemonify.NewExpense;
import lucastexiera.com.mschatbotopenai.dto.userwhatsapp.ChatbotMessage;
import lucastexiera.com.mschatbotopenai.infra.openfeign.FinanceClient;
import lucastexiera.com.mschatbotopenai.infra.openfeign.MonifyGatewayClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatFunctionHandlerService {


    private static final Logger log = LoggerFactory.getLogger(ChatFunctionHandlerService.class);

    @Autowired
    private FinanceClient financeClient;


    @Autowired
    private ConversationService conversationService;

    private final ObjectMapper objectMapper = new ObjectMapper();


    public ChatbotMessage SaveNewExpense(OpenAiMessageResponse OpenAiResponse, List<CategoryDTO> userListCategories, String from) throws JsonProcessingException {
        var expenseToBeSavedJson = OpenAiResponse.output().get(0).arguments();
        ChatbotMessage chatbotMessage;

        log.info("Despesa a ser salva: {}", expenseToBeSavedJson);

        var expenseToBeSaved = objectMapper.readValue(expenseToBeSavedJson, NewExpense.class);

        if (validateCategory(userListCategories, expenseToBeSaved)) {
            throw new RuntimeException("Categoria invalida");
        }

        financeClient.saveNewExpense(expenseToBeSaved);
        chatbotMessage = new ChatbotMessage("Prontinho! Sua despesa foi salva. Quer cadastrar mais alguma?");
        conversationService.saveAssistantMessage(from, chatbotMessage.message());

        return chatbotMessage;
    }


    public ChatbotMessage saveNewCategory(OpenAiMessageResponse OpenAiResponse, String from) throws JsonProcessingException {
        var categoryTolBeSavedJson =  OpenAiResponse.output().get(0).arguments();

        var expenseToBeSaved = objectMapper.readValue(categoryTolBeSavedJson, CategoryDTO.class);

        financeClient.saveNewCategory(expenseToBeSaved);
        log.info("Category a ser salva: {}", categoryTolBeSavedJson);

        var chatbotMessage = new ChatbotMessage("Sua categoria " + expenseToBeSaved.category_name() + " foi cadastrada com sucesso!");
        conversationService.saveAssistantMessage(from, chatbotMessage.message());
        return chatbotMessage;

    }

    public ChatbotMessage deleteCategory(OpenAiMessageResponse OpenAiResponse, String from) throws JsonProcessingException {
        var categoryTolBeDeletedJson = OpenAiResponse.output().get(0).arguments();

        var expenseToBeDeleted = objectMapper.readValue(categoryTolBeDeletedJson, CategoryDTO.class);

        financeClient.deleteCategory(expenseToBeDeleted);
        log.info("Category a ser excluida: {}", expenseToBeDeleted);

        var chatbotMessage = new ChatbotMessage("Sua categoria " + expenseToBeDeleted.category_name() + " foi excluída com sucesso!");
        conversationService.saveAssistantMessage(from, chatbotMessage.message());
        return chatbotMessage;

    }

    public boolean validateCategory(List<CategoryDTO> userListCategories, NewExpense expenseToBeSaved) {
        return userListCategories.stream()
                .anyMatch(c -> c.category_id().equals(expenseToBeSaved.category_id()));
    }
}
