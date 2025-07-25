package lucastexiera.com.mswhatsapp.dto.whatsapp;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record WhatsappWebhookRequest(
    String event,
    String body,
    String type,
    String from,
    Boolean isGroupMsg
) {
}
