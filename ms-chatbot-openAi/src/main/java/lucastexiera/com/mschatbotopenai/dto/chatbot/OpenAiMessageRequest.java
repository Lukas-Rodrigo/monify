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

}
