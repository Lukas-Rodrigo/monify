package lucastexiera.com.mswhatsapp.service;


import lucastexiera.com.mswhatsapp.dto.Chatbot.ChatBotRequest;
import lucastexiera.com.mswhatsapp.dto.Chatbot.ChatBotResponse;
import lucastexiera.com.mswhatsapp.dto.whatsapp.WhatsAppMessageRequest;
import lucastexiera.com.mswhatsapp.infra.openfeign.ChatbotClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Service
public class WhatsappService {

    private static final Logger log = LoggerFactory.getLogger(WhatsappService.class);


    @Autowired
    private ChatbotClient chatbotClient;

    public ChatBotResponse processIncomingMessage(
        WhatsAppMessageRequest messageRequest
    ) {
        if (validateUserMessage(messageRequest)) {
            throw new RuntimeException("Tipo de mensagem invalida");
        }

        var userMessage = messageRequest.body();
        var from = extractPhoneNumber(messageRequest.from());

        log.info(
            "userMessage: {}",
            userMessage
        );

        log.info(
            "from: {}",
            from
        );

        var chatbotMessage =
            chatbotClient.sendMessageToChatBot(new ChatBotRequest(userMessage
                , from));

        log.info("chatbotRequest: {}", chatbotMessage);

        return chatbotMessage;

    }


    private boolean validateUserMessage(WhatsAppMessageRequest messageRequest) {
        return !Objects.equals(
            messageRequest.event(),
            "onmessage"
        ) || messageRequest.isGroupMsg() || messageRequest
            .type()
            .equals(
                "ptt");
    }

    private String extractPhoneNumber(String phoneNumber) {
        return phoneNumber.replaceAll(
            "@.*",
            ""
        );
    }

}

