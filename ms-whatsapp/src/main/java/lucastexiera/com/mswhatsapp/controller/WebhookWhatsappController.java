package lucastexiera.com.mswhatsapp.controller;

import lucastexiera.com.mswhatsapp.dto.whatsapp.WhatsappWebhookResponse;
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
    private WhatsappService whatsappService;

    @Value("${whatsapp.api.myTokenVerification}")
    private String MY_TOKEN_VERIFICATION;

    private static final Logger log = LoggerFactory.getLogger(WebhookWhatsappController.class);


    @GetMapping()
    public ResponseEntity<String> verifyWebhook(
            @RequestParam("hub.mode") String mode,
            @RequestParam("hub.challenge") String challenge,
            @RequestParam("hub.verify_token") String token

    ) {

        if (mode.equals("subscribe") && token.equals(MY_TOKEN_VERIFICATION)) {
            log.info("Webhook received");
            return ResponseEntity.ok(challenge);
        } else {
            log.info("webhook not received");
            return ResponseEntity.status(403).build();
        }
    }

    @PostMapping
    public ResponseEntity<Void> receiveWhatsappMessage(@RequestBody WhatsappWebhookResponse payload) {
        whatsappService.processIncomingMessage(payload);
        return ResponseEntity.ok().build();
    }

}