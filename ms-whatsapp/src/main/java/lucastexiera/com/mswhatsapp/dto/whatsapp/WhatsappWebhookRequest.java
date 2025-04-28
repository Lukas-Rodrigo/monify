package lucastexiera.com.mswhatsapp.dto.whatsapp;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record WhatsappWebhookRequest(
        @NotNull @Valid List<Entry> entry
) {
    public record Entry(
            @NotNull @Valid List<Change> changes
    ) {}

    public record Change(
            @NotNull @Valid ChangeValue value
    ) {}

    public record ChangeValue(
            @NotNull @Valid List<Message> messages
    ) {}

    public record Message(
            @NotNull String from,
            @NotNull @Valid MessageText text
    ) {}

    public record MessageText(
            @NotNull String body
    ) {}
}
