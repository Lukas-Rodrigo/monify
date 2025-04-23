package lucastexiera.com.mswhatsapp.dto.whatsapp;

import java.util.List;

public record WhatsappWebhookResponse(

        List<Entry> entry
) {
    public record Entry(List<Change> changes) {}
    public record Change(ChangeValue value) {}
    public record ChangeValue(List<Message> messages) {}
    public record Message(String from, MessageText text) {}
    public record MessageText(String body) {}

}