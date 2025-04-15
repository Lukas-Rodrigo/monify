package lucastexiera.com.mschatbotopenai.dto;

import java.util.List;

public record OpenAiMessageRequest(
        String model,
        List<Message> input,
        Text text
) {
    public record Message(
            String role,
            List<Content> content
    ) {}

    public record Content(
            String type,
            String text
    ) {
        public static Content of(String text) {
            return new Content("input_text", text);
        }
    }

    public record Text(
            Format format
    ) {}

    public record Format(
            String type
    ) {}

    public static OpenAiMessageRequest of(List<Message> input) {
        var chatAssistant = new Message(
                "system",
                List.of(new Content("input_text",
                        "Você é um chatbot que salva gastos e me dá relatórios. Quando o usuário falar que quer um relatório ou que gastou algo, isso deve ser salvo no banco de dados. Retorne de forma gentil para o usuário."
                ))
        );

        List<Message> allMessages = new java.util.ArrayList<>();
        allMessages.add(chatAssistant);
        allMessages.addAll(input);

        return new OpenAiMessageRequest(
                "gpt-3.5-turbo",
                allMessages,
                new Text(new Format("text"))
        );
    }
}
