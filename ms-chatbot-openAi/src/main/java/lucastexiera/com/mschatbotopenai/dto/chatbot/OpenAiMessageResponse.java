package lucastexiera.com.mschatbotopenai.dto.chatbot;

import java.util.List;

public record OpenAiMessageResponse(
        long created,
        String model,
        List<Choice> choices
) {
    public record Choice(
            int index,
            Message message
    ) {}

    public record Message(
            String role,
            String content,
            List<ToolCall> tool_calls
    ) {}

    public record ToolCall(
            String id,
            String type,
            Function function
    ) {}

    public record Function(
            String name,
            String arguments
    ) {}
}
