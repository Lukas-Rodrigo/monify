package lucastexiera.com.mschatbotopenai.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lucastexiera.com.mschatbotopenai.dto.chatbot.OpenAiMessageResponse;
import lucastexiera.com.mschatbotopenai.dto.financemonify.CategoryDTO;
import lucastexiera.com.mschatbotopenai.dto.userwhatsapp.ChatbotMessage;
import lucastexiera.com.mschatbotopenai.dto.userwhatsapp.WhatsappUserMessageResponse;

import java.util.List;

public interface TypeMessageStrategy {

  ChatbotMessage handleTypeMessage(OpenAiMessageResponse openAiMessageResponse,
                                   WhatsappUserMessageResponse userMessage,
                                   List<CategoryDTO> userListCategories
                                  );
}
