package lucastexiera.com.mswhatsapp.controller;

import lucastexiera.com.mswhatsapp.dto.Chatbot.ChatBotResponse;
import lucastexiera.com.mswhatsapp.dto.whatsapp.WhatsAppMessageRequest;
import lucastexiera.com.mswhatsapp.dto.whatsapp.WhatsappWebhookRequest;
import lucastexiera.com.mswhatsapp.service.WhatsappService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/webhook/whatsapp")
public class WebhookWhatsappController {

    @Autowired
    private  WhatsappService whatsappService;


    @PostMapping
    public ResponseEntity<Void> receiveWhatsappMessage(
        @RequestBody WhatsappWebhookRequest payload
    ) {
        whatsappService.processIncomingMessage(payload);
        return ResponseEntity.ok().build();
    }

}