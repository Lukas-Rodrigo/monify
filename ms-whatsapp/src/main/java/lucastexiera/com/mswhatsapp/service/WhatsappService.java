package lucastexiera.com.mswhatsapp.service;


import lucastexiera.com.mswhatsapp.dto.users.UserRequest;
import lucastexiera.com.mswhatsapp.dto.whatsapp.WhatsAppMessageRequest;
import lucastexiera.com.mswhatsapp.dto.whatsapp.WhatsappWebhookResponse;
import lucastexiera.com.mswhatsapp.infra.openfeign.ChatbotClient;
import lucastexiera.com.mswhatsapp.infra.openfeign.UserClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
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

    @Autowired
    private UserClient userClient;


    public void processIncomingMessage(WhatsappWebhookResponse payload) {

        log.info("dados recebidos: {}", payload);

        try {

            var entryList = payload.entry();
            if (entryList == null || entryList.isEmpty()) return;

            var changeList = entryList.get(0).changes();
            if (changeList == null || changeList.isEmpty()) return;

            var messageValues = changeList.get(0).value();
            if (messageValues == null || messageValues.messages() == null || messageValues.messages().isEmpty()) return;

            var message = messageValues.messages().get(0);
            String from = message.from();
            String userMessage = message.text().body();

            log.info("from: {}", from);
            log.info("userMessage: {}", userMessage);

            var dataFromWhatsappUser = new UserRequest("User1", from);
            var listCategory = userClient.findUserCategories(dataFromWhatsappUser);
            log.info("listCategory: {}", listCategory.getBody());

        } catch (Exception e) {
            e.printStackTrace();
        }
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

