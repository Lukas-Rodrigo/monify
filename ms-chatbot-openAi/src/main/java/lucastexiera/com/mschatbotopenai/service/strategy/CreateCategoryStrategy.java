package lucastexiera.com.mschatbotopenai.service.strategy;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lucastexiera.com.mschatbotopenai.dto.chatbot.OpenAiMessageResponse;
import lucastexiera.com.mschatbotopenai.dto.financemonify.CategoryDTO;
import lucastexiera.com.mschatbotopenai.dto.userwhatsapp.ChatbotMessage;
import lucastexiera.com.mschatbotopenai.dto.userwhatsapp.WhatsappUserMessageResponse;
import lucastexiera.com.mschatbotopenai.service.ChatBotFunctionStrategy;
import lucastexiera.com.mschatbotopenai.service.ChatFunctionHandlerService;

import java.util.List;

@RequiredArgsConstructor
public class CreateCategoryStrategy implements ChatBotFunctionStrategy {

    private final ChatFunctionHandlerService functionHandlerService;

    @Override
    public ChatbotMessage handle(OpenAiMessageResponse openAiResponse, List<CategoryDTO> userCategories, WhatsappUserMessageResponse userMessage) throws JsonProcessingException {
        return functionHandlerService.saveNewCategory(openAiResponse, userMessage.from());
    }
}
