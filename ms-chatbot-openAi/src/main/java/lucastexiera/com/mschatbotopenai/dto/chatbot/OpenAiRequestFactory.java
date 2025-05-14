package lucastexiera.com.mschatbotopenai.dto.chatbot;

import lucastexiera.com.mschatbotopenai.dto.financemonify.CategoryDTO;
import lucastexiera.com.mschatbotopenai.model.ChatMessage;
import lucastexiera.com.mschatbotopenai.model.Conversation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OpenAiRequestFactory {

    private static final String SYSTEM_PROMPT =
            "Você é um assistente financeiro amigável chamado Monify. Seu objetivo é ajudar o usuário a registrar despesas e categorias de forma prática e eficiente. " +
                    "Sempre que o usuário mencionar uma nova despesa, você deve extrair a descrição, o valor (em R$) e a categoria correspondente. " +
                    "Utilize a função 'enviar_despesa' para registrar essas informações. " +
                    "Sempre exiba as categorias disponíveis numeradas, uma por linha "+
                    "As categorias disponíveis para este usuário são: %s. " +
                    "O usuário pode atualizar somente a última despesa. Chame a função 'update_last_category'. " +
                    "Se a categoria mencionada pelo usuário não estiver na lista, peça gentilmente para ele escolher uma das disponíveis. " +
                    "Se a descrição, o valor ou a categoria estiverem ausentes, solicite educadamente que o usuário informe os dados faltantes. " +
                    "Se o usuário quiser incluir uma categoria que já está na lista, fale gentilmente que ele já possui essa categoria. " +
                    "voce so pode responder perguntas que corresponda a esse contexto.";


    private static final Logger log = LoggerFactory.getLogger(OpenAiRequestFactory.class);

    public static OpenAiMessageRequest instance(List<ChatMessage> conversation, List<CategoryDTO> userCategories) {

        String formattedCategories = userCategories.stream()
                .map(cat -> "- " + "category_id:" + cat.category_id() + " name: " +cat.name())
                .collect(Collectors.joining("\n"));

        String systemText = String.format(SYSTEM_PROMPT, formattedCategories);
        OpenAiMessageRequest.Message systemMessage = new OpenAiMessageRequest.Message(
                "system",
                systemText
        );

        List<OpenAiMessageRequest.Tool> tools = Arrays.stream(ChatbotToolDef.values()).map(OpenAiRequestFactory::buildTool).toList();


        List<OpenAiMessageRequest.Message> allMessages = new ArrayList<>();
        var historyMessages = mapConversationHistory(conversation);
        allMessages.add(systemMessage);
        allMessages.addAll(historyMessages);

        return new OpenAiMessageRequest(
                "gpt-4.1",
                allMessages,
                tools,
            "auto"
            );
    }

    public static List<OpenAiMessageRequest.Message> mapConversationHistory(List<ChatMessage> conversation) {
        return conversation.stream().map(message -> new OpenAiMessageRequest.Message(
                message.getSender(),
                message.getMessage()
        )).toList();
    }

    private static OpenAiMessageRequest.Tool buildTool(ChatbotToolDef toolDef) {
        return new OpenAiMessageRequest.Tool(
                "function",
                new OpenAiMessageRequest.Function(
                        toolDef.name,
                        toolDef.description,
                        new OpenAiMessageRequest.Parameters(
                                "object",
                                toolDef.required,
                                toolDef.properties,
                                false
                )

                )
        );
    }
}
