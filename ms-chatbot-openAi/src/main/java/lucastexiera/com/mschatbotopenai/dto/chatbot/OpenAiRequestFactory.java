package lucastexiera.com.mschatbotopenai.dto.chatbot;

import lucastexiera.com.mschatbotopenai.dto.financemonify.CategoryDTO;
import lucastexiera.com.mschatbotopenai.model.Conversation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OpenAiRequestFactory {

    private static final String SYSTEM_PROMPT = """
            Você é Monify, um assistente financeiro amigável. Seu objetivo é ajudar o usuário a
            registrar suas despesas de forma prática e eficiente.
            
            Sempre que o usuário mencionar uma nova despesa, extraia:
            - A descrição
            - O valor (em R$)
            - A categoria correspondente
            
            Use a função 'enviar_despesa' para registrar essas informações.
            
            O usuário deve sempre informar os três dados: descrição, valor e categoria. Caso falte
            algum deles, solicite educadamente que complete.
            
            As categorias disponíveis para este usuário são:
            exiba em ordem numerica sempre.
            %s
            
            Sempre verifique se a categoria informada está na lista. Caso o usuário mencione uma
            categoria inexistente, oriente-o a usar uma das disponíveis ou a cadastrar uma nova.
             
            Antes de chamar as funcoes corrija os erros de portugues/ingles -> categoria, descricao.
            
            O usuário pode atualizar apenas a última despesa cadastrada usando a função
            'update_last_category'.
            
            Caso o usuário tente adicionar uma categoria que já existe, informe de forma amigável
            que ela já está cadastrada.
            """;


    private static final Logger log = LoggerFactory.getLogger(OpenAiRequestFactory.class);

    public static OpenAiMessageRequest instance(Conversation conversation, List<CategoryDTO> userCategories) {

        String formattedCategories = userCategories.stream()
                .map(cat -> "- " + cat)
                .collect(Collectors.joining("\n"));

        String systemText = String.format(SYSTEM_PROMPT, formattedCategories);
        OpenAiMessageRequest.Message systemMessage = new OpenAiMessageRequest.Message(
                "assistant",
                List.of(OpenAiMessageRequest.Content.fromRole(systemText, "assistant"))
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
                new OpenAiMessageRequest.Text(new OpenAiMessageRequest.Format("text")),
                "auto"
        );
    }

    public static List<OpenAiMessageRequest.Message> mapConversationHistory(Conversation conversation) {
        return conversation.getMessages().stream()
                .map(m -> new OpenAiMessageRequest.Message(
                        m.getSender(),
                        List.of(OpenAiMessageRequest.Content.fromRole(m.getMessage(), m.getSender()))
                ))
                .toList();
    }

    private static OpenAiMessageRequest.Tool buildTool(ChatbotToolDef toolDef) {
        return new OpenAiMessageRequest.Tool(
                "function",
                toolDef.name,
                toolDef.description,
                new OpenAiMessageRequest.Parameters(
                        "object",
                        toolDef.required,
                        toolDef.properties,
                        false

                ),
                true
        );
    }


}
