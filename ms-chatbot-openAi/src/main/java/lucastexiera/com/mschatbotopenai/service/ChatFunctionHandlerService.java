package lucastexiera.com.mschatbotopenai.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lucastexiera.com.mschatbotopenai.dto.chatbot.OpenAiMessageResponse;
import lucastexiera.com.mschatbotopenai.dto.financemonify.CategoryDTO;
import lucastexiera.com.mschatbotopenai.dto.financemonify.ExpenseDTO;
import lucastexiera.com.mschatbotopenai.dto.userwhatsapp.ChatbotMessage;
import lucastexiera.com.mschatbotopenai.infra.openfeign.FinanceClient;
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

    @Autowired
    private AnswersForUsersService answersForUsersService;

    @Autowired
    private UsersService usersService;

    private final ObjectMapper objectMapper = new ObjectMapper();


    public ChatbotMessage SaveNewExpense(OpenAiMessageResponse OpenAiResponse, List<CategoryDTO> userListCategories, String from) throws JsonProcessingException {
        var expenseToBeSavedJson = OpenAiResponse.output().get(0).arguments();
        var userId = usersService.UserByPhoneNumber(from);

        var expenseToBeSaved = objectMapper.readValue(expenseToBeSavedJson, ExpenseDTO.class);

        if (!validateCategory(userListCategories, expenseToBeSaved)) {
            throw new RuntimeException("Categoria invalida");
        }
        var categoryName = hasNameCategoryReturn(userListCategories, expenseToBeSaved);

        financeClient.saveNewExpense(expenseToBeSaved, userId);
        var chatbotMessage = answersForUsersService.newExpenseMessage(expenseToBeSaved, categoryName);
        conversationService.saveAssistantMessage(from, chatbotMessage.message());

        return chatbotMessage;
    }

    public ChatbotMessage updateLastCategory(OpenAiMessageResponse OpenAiResponse, List<CategoryDTO> userListCategories ,String from) throws JsonProcessingException {
        var userId = usersService.UserByPhoneNumber(from);

        var expenseTolBeUpdateJson =  OpenAiResponse.output().get(0).arguments();

        var expenseToBeUpdate = objectMapper.readValue(expenseTolBeUpdateJson, ExpenseDTO.class);

        var categoryName = hasNameCategoryReturn(userListCategories, expenseToBeUpdate);

        financeClient.updateLastExpense(userId ,expenseToBeUpdate);
        log.info("Category a ser atualizada: {}", expenseTolBeUpdateJson);

        var chatbotMessage = answersForUsersService.updateLastExpense(expenseToBeUpdate,categoryName);
        conversationService.saveAssistantMessage(from, chatbotMessage.message());
        return chatbotMessage;

    }


    public ChatbotMessage saveNewCategory(OpenAiMessageResponse OpenAiResponse, String from) throws JsonProcessingException {
        var userId = usersService.UserByPhoneNumber(from);
        var categoryTolBeSavedJson =  OpenAiResponse.output().get(0).arguments();

        var expenseToBeSaved = objectMapper.readValue(categoryTolBeSavedJson, CategoryDTO.class);

        financeClient.saveNewCategory(expenseToBeSaved, userId);
        log.info("Category a ser salva: {}", categoryTolBeSavedJson);

        var chatbotMessage = answersForUsersService.newCategoryMessage(expenseToBeSaved);
        conversationService.saveAssistantMessage(from, chatbotMessage.message());
        return chatbotMessage;

    }

    public ChatbotMessage deleteCategory(OpenAiMessageResponse OpenAiResponse, String from) throws JsonProcessingException {
        var categoryTolBeDeletedJson = OpenAiResponse.output().get(0).arguments();


        var expenseToBeDeleted = objectMapper.readValue(categoryTolBeDeletedJson, CategoryDTO.class);

        financeClient.deleteCategory(expenseToBeDeleted);
        log.info("Category a ser excluida: {}", expenseToBeDeleted);

        var chatbotMessage = answersForUsersService.deleteCategoryMessage(expenseToBeDeleted);
        conversationService.saveAssistantMessage(from, chatbotMessage.message());
        return chatbotMessage;

    }



    public boolean validateCategory(List<CategoryDTO> userListCategories, ExpenseDTO expenseToBeSaved) {
        return userListCategories.stream()
                .anyMatch(c -> c.category_id().equals(expenseToBeSaved.category_id()));
    }

    public CategoryDTO hasNameCategoryReturn(List<CategoryDTO> userCategories, ExpenseDTO newExpense) {
        return userCategories.stream()
                .filter(category -> category.category_id().equals(newExpense.category_id()))
                .findFirst()
                .orElse(null);
    }

}
