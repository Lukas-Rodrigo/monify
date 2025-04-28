package lucastexiera.com.mswhatsapp.service;


import lucastexiera.com.mswhatsapp.dto.Chatbot.ChatBotRequest;
import lucastexiera.com.mswhatsapp.dto.whatsapp.WhatsAppMessageRequest;
import lucastexiera.com.mswhatsapp.dto.whatsapp.WhatsappWebhookRequest;
import lucastexiera.com.mswhatsapp.infra.openfeign.ChatbotClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.client.RestTemplate;

@Service
public class WhatsappService {

    private static final Logger log = LoggerFactory.getLogger(WhatsappService.class);


    @Value("${whatsapp.api.baseURL}")
    private String WHATSAPP_API_URL;

    @Value("${whatsapp.api.numberID}")
    private String PHONE_NUMBER_ID;

    @Value("${whatsapp.api.token}")
    private String WHATSAPP_API_KEY;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ChatbotClient chatbotClient;


    public void processIncomingMessage(WhatsappWebhookRequest payload) {

        log.info("dados recebidos: {}", payload);

        var entryList = payload.entry();

        var changeList = entryList.get(0).changes();

        var messageValues = changeList.get(0).value();

        var message = messageValues.messages().get(0);
        var from = message.from();
        String userMessage = message.text().body();

        log.info("from: {}", from);
        log.info("userMessage: {}", userMessage);

        var request = new ChatBotRequest(userMessage, from);
        var chatBotMessage = chatbotClient.sendoMessageToChatBot(request);

        log.info("chatBotMessage: {}", chatBotMessage);
        sendMessage(from, chatBotMessage.message());

    }

    public void sendMessage(String to, String message) {
        String url = WHATSAPP_API_URL + PHONE_NUMBER_ID + "/messages";
        log.info("Mensagem para o número: " + to);
        log.info("Mensagem: " + message);
        var messageRequest = WhatsAppMessageRequest.of(to, message);
        HttpEntity<WhatsAppMessageRequest> request = new HttpEntity<>(messageRequest);
        restTemplate.postForEntity(url, request, String.class);
    }


}

