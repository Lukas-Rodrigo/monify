package lucastexiera.com.mschatbotopenai.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lucastexiera.com.mschatbotopenai.dto.chatbot.OpenAiMessageResponse;
import lucastexiera.com.mschatbotopenai.dto.financemonify.CategoryDTO;
import lucastexiera.com.mschatbotopenai.dto.financemonify.ExpenseDTO;
import lucastexiera.com.mschatbotopenai.dto.userwhatsapp.ChatbotMessage;
import lucastexiera.com.mschatbotopenai.exceptions.InvalidJsonFormatException;
import lucastexiera.com.mschatbotopenai.infra.openfeign.FinanceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ToolHandleService {


  private static final Logger log = LoggerFactory.getLogger(ToolHandleService.class);

  @Autowired
  private FinanceClient financeClient;


  @Autowired
  private AnswersForUsersService answersForUsersService;

  @Autowired
  private UsersService usersService;

  private final ObjectMapper objectMapper = new ObjectMapper();


  public ChatbotMessage SaveNewExpense(
      OpenAiMessageResponse OpenAiResponse,
      List<CategoryDTO> userListCategories, String from
  ) {
    var expenseToBeSavedJson = OpenAiResponse
        .choices()
        .get(0)
        .message()
        .tool_calls()
        .get(0)
        .function()
        .arguments();
    var userId = usersService.findUserIDByPhoneNumber(from);

    var expenseToBeSaved = parseJson(expenseToBeSavedJson, ExpenseDTO.class);

    log.info(
        "Expense To Be Saved: {}",
        expenseToBeSaved
    );

    if (!validateCategory(
        userListCategories,
        expenseToBeSaved
    )) {
      throw new RuntimeException("Categoria invalida");
    }
    var categoryName = hasNameCategoryReturn(
        userListCategories,
        expenseToBeSaved
    );

    financeClient.saveNewExpense(
        expenseToBeSaved,
        userId
    );
    return answersForUsersService.confirmNewExpense(
        expenseToBeSaved,
        categoryName
    );

  }

  public ChatbotMessage updateLastCategory(
      OpenAiMessageResponse OpenAiResponse,
      List<CategoryDTO> userListCategories, String from
  ) {
    var userId = usersService.findUserIDByPhoneNumber(from);

    var expenseTolBeUpdateJson = OpenAiResponse
        .choices()
        .get(0)
        .message()
        .tool_calls()
        .get(0)
        .function()
        .arguments();
    ;

    var expenseToBeUpdate = parseJson(
        expenseTolBeUpdateJson,
        ExpenseDTO.class
    );

    var categoryName = hasNameCategoryReturn(
        userListCategories,
        expenseToBeUpdate
    );

    financeClient.updateLastExpense(
        userId,
        expenseToBeUpdate
    );
    log.info(
        "Category a ser atualizada: {}",
        expenseTolBeUpdateJson
    );

    return answersForUsersService.confirmUpdateExpense(
        expenseToBeUpdate,
        categoryName
    );

  }


  public ChatbotMessage saveNewCategory(
      OpenAiMessageResponse OpenAiResponse,
      String from
  ) {
    var userId = usersService.findUserIDByPhoneNumber(from);
    var categoryTolBeSavedJson = OpenAiResponse
        .choices()
        .get(0)
        .message()
        .tool_calls()
        .get(0)
        .function()
        .arguments();

    var expenseToBeSaved = parseJson(
        categoryTolBeSavedJson,
        CategoryDTO.class
    );

    financeClient.saveNewCategory(
        expenseToBeSaved,
        userId
    );
    log.info(
        "Category a ser salva: {}",
        categoryTolBeSavedJson
    );

    return answersForUsersService.confirmNewCategory(expenseToBeSaved);
  }

  public ChatbotMessage deleteCategory(
      OpenAiMessageResponse OpenAiResponse,
      String from
  ) {
    var categoryTolBeDeletedJson = OpenAiResponse
        .choices()
        .get(0)
        .message()
        .tool_calls()
        .get(0)
        .function()
        .arguments();

    var expenseToBeDeleted = parseJson(
        categoryTolBeDeletedJson,
        CategoryDTO.class
    );

    financeClient.deleteCategory(expenseToBeDeleted);
    log.info(
        "Category a ser excluida: {}",
        expenseToBeDeleted
    );

    return answersForUsersService.confirmDeleteCategory(expenseToBeDeleted);
  }


  public boolean validateCategory(
      List<CategoryDTO> userListCategories, ExpenseDTO expenseToBeSaved) {
    return userListCategories
        .stream()
        .anyMatch(c -> c
            .category_id()
            .equals(expenseToBeSaved.category_id()));
  }

  public CategoryDTO hasNameCategoryReturn(
      List<CategoryDTO> userCategories, ExpenseDTO newExpense) {
    return userCategories
        .stream()
        .filter(category -> category
            .category_id()
            .equals(newExpense.category_id()))
        .findFirst()
        .orElse(null);
  }

  private <T> T parseJson(String json, Class<T> valueType) {
    try {
      return objectMapper.readValue(
          json,
          valueType
      );
    } catch (JsonProcessingException e) {
      log.error(
          "Erro ao converter JSON para {}: {}",
          valueType.getSimpleName(),
          json,
          e
      );
      throw new InvalidJsonFormatException(
          "Erro ao processar a requisição. " +
              "JSON inválido.",
          e
      );
    }
  }

}
