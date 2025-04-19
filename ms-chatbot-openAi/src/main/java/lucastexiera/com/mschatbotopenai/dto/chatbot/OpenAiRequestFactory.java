package lucastexiera.com.mschatbotopenai.dto.chatbot;

import lucastexiera.com.mschatbotopenai.dto.financemonify.CategoryDTO;
import lucastexiera.com.mschatbotopenai.model.Conversation;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OpenAiRequestFactory {

    public static OpenAiMessageRequestt instance(Conversation conversation, List<CategoryDTO> userCategories) {

        var systemMessage = createSystemMessage(userCategories);

        var newExpenseTool = instanceNewExpenseTool();


        List<OpenAiMessageRequestt.Message> allMessages = new ArrayList<>();
        var historyMessages = mapConversationHistory(conversation);


        allMessages.add(systemMessage);
        allMessages.addAll(historyMessages);

        return new OpenAiMessageRequestt(
                "gpt-4.1",
                allMessages,
                List.of(newExpenseTool),
                new OpenAiMessageRequestt.Text(new OpenAiMessageRequestt.Format("text")),
                "auto"
        );
    }

    private static List<OpenAiMessageRequestt.Message> mapConversationHistory(Conversation conversation) {
        return conversation.getMessages().stream()
                .map(m -> new OpenAiMessageRequestt.Message(
                        m.getSender(),
                        List.of(OpenAiMessageRequestt.Content.fromRole(m.getMessage(), m.getSender()))
                ))
                .toList();
    }

    private static OpenAiMessageRequestt.Message createSystemMessage(List<CategoryDTO>  userCategories ) {
        String formattedCategories = userCategories.stream()
                .map(cat -> "- " + cat)
                .collect(Collectors.joining("\n"));

        return new OpenAiMessageRequestt.Message(
                "assistant",
                List.of(OpenAiMessageRequestt.Content.fromRole(
                        (
                                "Você é um assistente financeiro amigável chamado Monify. Seu objetivo é ajudar o usuário a registrar despesas de forma prática e eficiente. " +
                                        "Sempre que o usuário mencionar uma nova despesa, você deve extrair a descrição, o valor (em R$) e a categoria correspondente. " +
                                        "Utilize a função de ferramenta 'enviar_despesa' para registrar essas informações. " +
                                        "As categorias disponíveis para este usuário são: " + formattedCategories + ". " +
                                        "Se a categoria mencionada pelo usuário não estiver na lista, peça gentilmente para ele escolher uma das disponíveis. " +
                                        "Se o usuário não mencionar o valor do gasto, peça gentilmente para ele.")
                        , "assistant")
                ));

    }

    private static OpenAiMessageRequestt.Tool instanceNewExpenseTool() {

        return new OpenAiMessageRequestt.Tool(
                "function",
                "enviar_despesa",
                "Envia os dados formatados da despesa para o backend de acordo com a categoria selecionada",
                new OpenAiMessageRequestt.Parameters(
                        "object",
                        List.of("description", "amount", "category_id"),
                        new OpenAiMessageRequestt.Properties(
                                new OpenAiMessageRequestt.Property("string", "Descrição da despesa"),
                                new OpenAiMessageRequestt.Property("number", "Valor da despesa"),
                                new OpenAiMessageRequestt.Property("number", "Identificador da categoria selecionada")
                        ),
                        false
                ),
                true
        );

    }


}
