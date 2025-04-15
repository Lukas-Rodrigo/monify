package lucastexiera.com.mschatbotopenai.dto;

import java.util.List;

public record OpenAiMessageResponse(
        String id,
        String status,
        List<Output> output
) {
    public record Output(
            String type,
            String role,
            List<Content> content,
            String name,
            String arguments
    ) {}

    public record Content(
            String type,
            String text
    ) {}
}