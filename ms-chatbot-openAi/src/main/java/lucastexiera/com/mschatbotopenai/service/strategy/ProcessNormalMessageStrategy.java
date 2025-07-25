package lucastexiera.com.mschatbotopenai.service.strategy;

import lombok.RequiredArgsConstructor;
import lucastexiera.com.mschatbotopenai.dto.chatbot.OpenAiMessageResponse;
import lucastexiera.com.mschatbotopenai.dto.financemonify.CategoryDTO;
import lucastexiera.com.mschatbotopenai.dto.userwhatsapp.ChatbotMessage;
import lucastexiera.com.mschatbotopenai.dto.userwhatsapp.WhatsappUserMessageResponse;
import lucastexiera.com.mschatbotopenai.service.ProcessUserMessageService;
import lucastexiera.com.mschatbotopenai.service.TypeMessageStrategy;

import java.util.List;


@RequiredArgsConstructor
public class ProcessNormalMessageStrategy implements TypeMessageStrategy {

  private final ProcessUserMessageService processUserMessage;

  @Override
  public ChatbotMessage handleTypeMessage(
      OpenAiMessageResponse openAiMessageResponse,
      WhatsappUserMessageResponse userMessage,
      List<CategoryDTO> userListCategories
  ) {
    return processUserMessage.processNormalMessage(
        openAiMessageResponse,
        userMessage
    );
  }
}
