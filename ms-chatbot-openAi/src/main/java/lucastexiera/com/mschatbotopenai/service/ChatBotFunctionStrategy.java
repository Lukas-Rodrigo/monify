package lucastexiera.com.mschatbotopenai.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lucastexiera.com.mschatbotopenai.dto.chatbot.OpenAiMessageResponse;
import lucastexiera.com.mschatbotopenai.dto.financemonify.CategoryDTO;
import lucastexiera.com.mschatbotopenai.dto.userwhatsapp.ChatbotMessage;
import lucastexiera.com.mschatbotopenai.dto.userwhatsapp.WhatsappUserMessageResponse;

import java.util.List;

public interface ChatBotFunctionStrategy {
    ChatbotMessage handle(OpenAiMessageResponse openAiResponse, List<CategoryDTO> userCategories, WhatsappUserMessageResponse userMessage) throws JsonProcessingException;
}
