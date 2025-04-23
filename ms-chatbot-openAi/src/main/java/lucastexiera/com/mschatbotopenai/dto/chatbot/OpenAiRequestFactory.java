package lucastexiera.com.mschatbotopenai.dto.chatbot;

import lucastexiera.com.mschatbotopenai.controller.OpenAiController;
import lucastexiera.com.mschatbotopenai.dto.financemonify.CategoryDTO;
import lucastexiera.com.mschatbotopenai.model.Conversation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OpenAiRequestFactory {

    private static final Logger log = LoggerFactory.getLogger(OpenAiRequestFactory.class);

    public static OpenAiMessageRequest instance(Conversation conversation, List<CategoryDTO> userCategories) {

        var systemMessage = createSystemMessage(userCategories);

        var newExpenseTool = instanceNewExpenseTool();
        var NewCategoryTool = instanceNewUserCategory();
        var deleteCategoryByIdTool = instanceDeleteCategoryById();


        List<OpenAiMessageRequest.Message> allMessages = new ArrayList<>();
        var historyMessages = mapConversationHistory(conversation);

        allMessages.add(systemMessage);
        allMessages.addAll(historyMessages);

        return new OpenAiMessageRequest(
                "gpt-4.1",
                allMessages,
                List.of(newExpenseTool, NewCategoryTool,  deleteCategoryByIdTool),
                new OpenAiMessageRequest.Text(new OpenAiMessageRequest.Format("text")),
                "auto"
        );
    }

    private static List<OpenAiMessageRequest.Message> mapConversationHistory(Conversation conversation) {
        return conversation.getMessages().stream()
                .map(m -> new OpenAiMessageRequest.Message(
                        m.getSender(),
                        List.of(OpenAiMessageRequest.Content.fromRole(m.getMessage(), m.getSender()))
                ))
                .toList();
    }

    private static OpenAiMessageRequest.Message createSystemMessage(List<CategoryDTO>  userCategories ) {
        String formattedCategories = userCategories.stream()
                .map(cat -> "- " + cat)
                .collect(Collectors.joining("\n"));

        log.info("Lista, {}", formattedCategories);

        return new OpenAiMessageRequest.Message(
                "assistant",
                List.of(OpenAiMessageRequest.Content.fromRole(
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

    private static OpenAiMessageRequest.Tool instanceNewExpenseTool() {

        Map<String, OpenAiMessageRequest.Property> propertiesNewExpense = Map.of(
                "description", new OpenAiMessageRequest.Property("string", "Descrição da despesa"),
                "amount", new OpenAiMessageRequest.Property("number", "Valor da despesa"),
                "category_id", new OpenAiMessageRequest.Property("number", "Identificador da categoria selecionada")

        );

        return new OpenAiMessageRequest.Tool(
                "function",
                "enviar_despesa",
                "Envia os dados formatados da despesa para o backend de acordo com a categoria selecionada",
                new OpenAiMessageRequest.Parameters(
                        "object",
                        List.of("description", "amount", "category_id"),
                        propertiesNewExpense,
                        false
                ),
                true
        );

    }

    private static  OpenAiMessageRequest.Tool instanceNewUserCategory() {
        Map<String, OpenAiMessageRequest.Property> propertiesNewCategory = Map.of(
                "name", new OpenAiMessageRequest.Property("string", "Nome da nova categoria a ser cadastrada")
        );

        return new OpenAiMessageRequest.Tool(
                "function",
                "create_category",
                "Cadastra uma nova categoria enviando seu nome para o backend",
                new OpenAiMessageRequest.Parameters(
                        "object",
                        List.of("name"),
                        propertiesNewCategory,
                        false
                ),
                true
        );
    }

    private static  OpenAiMessageRequest.Tool instanceDeleteCategoryById() {
        Map<String, OpenAiMessageRequest.Property> propertiesNewCategory = Map.of(
                "category_id", new OpenAiMessageRequest.Property("string", "ID único da categoria a ser excluída"),
                "category_name", new OpenAiMessageRequest.Property("string", "Nome da categoria a ser excluída")
        );

        return new OpenAiMessageRequest.Tool(
                "function",
                "delete_category",
                "Exclui uma categoria quando o usuário desejar, enviando o nome e ID da categoria para o backend.",
                new OpenAiMessageRequest.Parameters(
                        "object",
                        List.of("category_id", "category_name"),
                        propertiesNewCategory,
                        false
                ),
                true
        );
    }


}
