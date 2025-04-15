package lucastexiera.com.mswhatsapp.dto.whatsapp;

public record WhatsAppMessageRequest(
        String messaging_product,
        String recipient_type,
        String to,
        String type,
        Text text
) {
    public record Text(
            boolean preview_url,
            String body
    ) {}

    public static WhatsAppMessageRequest of(String to, String body) {
        return new WhatsAppMessageRequest(
                "whatsapp",
                "individual",
                to,
                "text",
                new Text(false, body)
        );
    }
}
