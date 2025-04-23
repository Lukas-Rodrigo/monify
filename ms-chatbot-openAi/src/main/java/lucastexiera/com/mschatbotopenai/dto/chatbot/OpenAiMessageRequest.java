package lucastexiera.com.mschatbotopenai.dto.chatbot;

import java.util.List;
import java.util.Map;

public record OpenAiMessageRequest(
        String model,
        List<Message> input,
        List<Tool> tools,
        Text text,
        String tool_choice
) {
    public record Message(String role, List<Content> content) {}

    public record Content(String type, String text) {

        public static Content fromRole(String text, String role) {
            String type = "assistant".equals(role) ? "output_text" : "input_text";
            return new Content(type, text);
        }

    }



    public record Text(Format format) {}

    public record Format(String type) {}

    public record Tool(
            String type,
            String name,
            String description,
            Parameters parameters,
            boolean strict
    ) {}

    public record Parameters(
            String type,
            List<String> required,
            Map<String, Property> properties,
            boolean additionalProperties
    ) {}


    public record Property(String type, String description) {}

    /*public static OpenAiMessageRequestt requestInstance(Conversation conversation, List<CategoryDTO> userCategories) {

        String formattedCategories = userCategories.stream()
                .map(cat -> "- " + cat)
                .collect(Collectors.joining("\n"));

        Tool enviarDespesa = new Tool(
                "function",
                "enviar_despesa",
                "Envia os dados formatados da despesa para o backend de acordo com a categoria selecionada",
                new Parameters(
                        "object",
                        List.of("description", "amount", "category_id"),
                        new Properties(
                                new Property("string", "Descrição da despesa"),
                                new Property("number", "Valor da despesa"),
                                new Property("number", "Identificador da categoria selecionada")
                        ),
                        false
                ),
                true
        );

        Message systemMessage = new Message(
                "assistant",
                List.of(new Content(
                        "Você é um assistente financeiro amigável chamado Monify. Seu objetivo é ajudar o usuário a registrar despesas de forma prática e eficiente. " +
                                "Sempre que o usuário mencionar uma nova despesa, você deve extrair a descrição, o valor (em R$) e a categoria correspondente. " +
                                "Utilize a função de ferramenta 'enviar_despesa' para registrar essas informações. " +
                                "As categorias disponíveis para este usuário são: " + formattedCategories + ". " +
                                "Se a categoria mencionada pelo usuário não estiver na lista, peça gentilmente para ele escolher uma das disponíveis. " +
                                "Se o usuário não mencionar o valor do gasto, peça gentilmente para ele."
                , "assistant"))
        );

        List<Message> allMessages = new ArrayList<>();
        allMessages.add(systemMessage);

        // Aqui adaptamos as mensagens da conversa
        List<Message> historyMessages = conversation.getMessages().stream()
                .map(m -> new Message(
                        m.getSender(),
                        List.of(new Content(m.getMessage(), m.getSender()))
                ))
                .toList();

        allMessages.addAll(historyMessages);

        return new OpenAiMessageRequestt(
                "gpt-4.1",
                allMessages,
                List.of(enviarDespesa),
                new Text(new Format("text")),
                "auto"
        );
    }*/
}
