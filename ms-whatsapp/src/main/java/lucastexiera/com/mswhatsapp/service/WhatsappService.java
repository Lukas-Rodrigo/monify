package lucastexiera.com.mswhatsapp.service;


import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lucastexiera.com.mswhatsapp.dto.Chatbot.ChatBotRequest;
import lucastexiera.com.mswhatsapp.dto.Chatbot.ChatBotResponse;
import lucastexiera.com.mswhatsapp.dto.whatsapp.WhatsAppMessageRequest;
import lucastexiera.com.mswhatsapp.dto.whatsapp.WhatsappWebhookRequest;
import lucastexiera.com.mswhatsapp.infra.openfeign.ChatbotClient;
import lucastexiera.com.mswhatsapp.infra.wpconnect.SessionTokenStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Service
@AllArgsConstructor
public class WhatsappService {

    private static final Logger log = LoggerFactory.getLogger(WhatsappService.class);

    private final RestTemplate restTemplate = new RestTemplate();

    private final String BASE_URL = "http://wppconnect-server:21465/api";

    private final SessionTokenStore sessionTokenStore;

    @Autowired
    private ChatbotClient chatbotClient;

    public void processIncomingMessage(
        WhatsappWebhookRequest messageRequest
    ) {
        if (validateUserMessage(messageRequest)) {
            throw new RuntimeException("Tipo de mensagem invalida");
        }

        var userMessage = messageRequest.body();
        var from = extractPhoneNumber(messageRequest.from());


        var chatbotMessage =
            chatbotClient.sendMessageToChatBot(new ChatBotRequest(userMessage
                ,from)).message();

        sendMessageToUser(chatbotMessage, from);
    }



    public void sendMessageToUser(String message, String from) {
        var url = BASE_URL + "/{sessionName}/send-message";
        var bodyRequest = new WhatsAppMessageRequest(from, message);
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(sessionTokenStore.getToken());
        var httpEntity = new HttpEntity<>(bodyRequest ,headers);

        var response = restTemplate.exchange(
            url,
            HttpMethod.POST,
            httpEntity,
            JsonNode.class,
            sessionTokenStore.getSessionName()
        );

        log.info("response message to user: {}", response.getBody());

    }




    private boolean validateUserMessage(WhatsappWebhookRequest messageRequest) {
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

