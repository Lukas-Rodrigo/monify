package lucastexiera.com.mschatbotopenai.service.strategy;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lucastexiera.com.mschatbotopenai.dto.chatbot.OpenAiMessageResponse;
import lucastexiera.com.mschatbotopenai.dto.financemonify.CategoryDTO;
import lucastexiera.com.mschatbotopenai.dto.userwhatsapp.ChatbotMessage;
import lucastexiera.com.mschatbotopenai.dto.userwhatsapp.WhatsappUserMessageResponse;
import lucastexiera.com.mschatbotopenai.service.ChatBotFunctionStrategy;
import lucastexiera.com.mschatbotopenai.service.ChatFunctionHandlerService;

import java.util.List;

@RequiredArgsConstructor
public class SaveNewExpenseStrategy implements ChatBotFunctionStrategy {


    private final ChatFunctionHandlerService functionHandlerService;

    @Override
    public ChatbotMessage handle(OpenAiMessageResponse openAiResponse, List<CategoryDTO> userListCategories, WhatsappUserMessageResponse userMessage) throws JsonProcessingException {
        return functionHandlerService.SaveNewExpense(openAiResponse, userListCategories, userMessage.from());
    }
}
