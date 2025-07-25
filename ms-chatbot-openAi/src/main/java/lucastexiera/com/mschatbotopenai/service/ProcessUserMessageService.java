package lucastexiera.com.mschatbotopenai.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lucastexiera.com.mschatbotopenai.dto.chatbot.OpenAiMessageResponse;
import lucastexiera.com.mschatbotopenai.dto.financemonify.CategoryDTO;
import lucastexiera.com.mschatbotopenai.dto.userwhatsapp.ChatbotMessage;
import lucastexiera.com.mschatbotopenai.dto.userwhatsapp.WhatsappUserMessageResponse;
import lucastexiera.com.mschatbotopenai.service.strategy.CreateCategoryStrategy;
import lucastexiera.com.mschatbotopenai.service.strategy.SaveNewExpenseStrategy;
import lucastexiera.com.mschatbotopenai.service.strategy.UpdateLastExpense;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class ProcessUserMessageService {


  private final ConversationService conversationService;

  private Map<String, ChatBotFunctionStrategy> mapStrategy;

  private final ToolHandleService functionHandlerService;

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

  }


  public ChatbotMessage processNormalMessage(
      OpenAiMessageResponse openAiResponse,
      WhatsappUserMessageResponse userMessage
  ) {
    var normalChatbotMessage =
        openAiResponse
            .choices()
            .get(0)
            .message()
            .content();

    conversationService.saveAssistantMessage(
        userMessage.from(),
        normalChatbotMessage
    );
    return new ChatbotMessage(normalChatbotMessage);

  }

  public ChatbotMessage processToolMessage(
      OpenAiMessageResponse openAiResponse,
      List<CategoryDTO> userListCategories,
      WhatsappUserMessageResponse userMessage
  ) {
    var toolName = openAiResponse
        .choices()
        .get(0)
        .message()
        .tool_calls()
        .get(0)
        .function()
        .name();

    var message = mapStrategy
        .get(toolName)
        .handle(
            openAiResponse,
            userListCategories,
            userMessage
        );
    conversationService.saveAssistantMessage(userMessage.from(), message.message());
    return message;

  }


}
