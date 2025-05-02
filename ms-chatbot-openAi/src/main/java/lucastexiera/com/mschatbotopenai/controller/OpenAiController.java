package lucastexiera.com.mschatbotopenai.controller;


import lucastexiera.com.mschatbotopenai.dto.userwhatsapp.ChatbotMessage;
import lucastexiera.com.mschatbotopenai.dto.userwhatsapp.WhatsappUserMessageResponse;
import lucastexiera.com.mschatbotopenai.service.OpenAiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/chatbot/message")

public class OpenAiController {

    @Autowired
    private OpenAiService openAiService;
    
    private static final Logger log = LoggerFactory.getLogger(OpenAiController.class);


    @PostMapping("/whatsapp")
    public ResponseEntity<ChatbotMessage> receivedMessageForMsWhatsapp(@RequestBody WhatsappUserMessageResponse payload) {
        var MessageToUse = openAiService.sendMessageOpenAi(payload);
        return ResponseEntity.ok(MessageToUse);

    }
}
