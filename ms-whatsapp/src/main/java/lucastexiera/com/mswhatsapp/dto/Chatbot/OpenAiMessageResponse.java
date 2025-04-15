package lucastexiera.com.mswhatsapp.dto.Chatbot;

import java.util.List;

public record OpenAiMessageResponse(
        String id,
        String status,
        List<Output> output
) {
    public record Output(
            String type,
            String role,
            List<Content> content
    ) {}

    public record Content(
            String type,
            String text
    ) {}
}