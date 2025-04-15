package lucastexiera.com.mschatbotopenai.dto;

import java.util.ArrayList;
import java.util.List;

public record OpenAiMessageRequestt(
        String model,
        List<Message> input,
        List<Tool> tools,
        Text text,
        String tool_choice
) {
    public record Message(String role, List<Content> content) {}

    public record Content(String type, String text) {
        public static Content of(String text) {
            return new Content("input_text", text);
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
            Properties properties,
            boolean additionalProperties
    ) {}

    public record Properties(Property description, Property amount, Property category_id) {}

    public record Property(String type, String description) {}

    public static OpenAiMessageRequestt requestInstance(List<Message> userMessages) {
        // Tool (função) para envio de despesas
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
                "system",
                List.of(Content.of(
                        "Você é um chatbot que salva gastos e me dá relatórios. Quando o usuário falar que quer um relatório ou que gastou algo, isso deve ser salvo no banco de dados."
                ))
        );

        List<Message> allMessages = new ArrayList<>();
        allMessages.add(systemMessage);
        allMessages.addAll(userMessages);

        return new OpenAiMessageRequestt(
                "gpt-3.5-turbo",
                allMessages,
                List.of(enviarDespesa),
                new Text(new Format("text")),
                "auto"
        );
    }
}
