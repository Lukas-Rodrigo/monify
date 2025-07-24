package lucastexiera.com.mswhatsapp.dto.whatsapp;

public record WhatsAppMessageRequest(
    String event,
    String body,
    String type,
    String from,
    Boolean isGroupMsg
) {

}
