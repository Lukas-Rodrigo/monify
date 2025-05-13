package lucastexiera.com.mschatbotopenai.dto.chatbot;

import java.util.List;
import java.util.Map;

public record OpenAiMessageRequest(
        String model,
        List<Message> messages,
        List<Tool> tools,
        String tool_choice
) {
    public record Message(String role, String content) {}

    public record Tool(String type, Function function) {}

    public record Function(
            String name,
            String description,
            Parameters parameters
    ) {}

    public record Parameters(
            String type,
            List<String> required,
            Map<String, Property> properties,
            boolean additionalProperties
    ) {}

    public record Property(String type, String description) {}
}
