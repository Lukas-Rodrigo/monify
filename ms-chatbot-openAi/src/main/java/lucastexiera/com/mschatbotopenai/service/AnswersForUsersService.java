package lucastexiera.com.mschatbotopenai.service;

import lucastexiera.com.mschatbotopenai.dto.chatbot.OpenAiMessageRequest;
import lucastexiera.com.mschatbotopenai.dto.chatbot.OpenAiMessageResponse;
import lucastexiera.com.mschatbotopenai.dto.financemonify.CategoryDTO;
import lucastexiera.com.mschatbotopenai.dto.financemonify.NewExpense;
import lucastexiera.com.mschatbotopenai.dto.userwhatsapp.ChatbotMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;


@Service
public class AnswersForUsersService {

    private static final Logger log = LoggerFactory.getLogger(AnswersForUsersService.class);

    @Autowired
    private ConversationService conversationService;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${openAi.api.baseURL}")
    private String OPENAI_URL;


    public ChatbotMessage newExpenseMessage(NewExpense newExpense, CategoryDTO categoryName) {
        var message = "Você é Monify, um assistente financeiro amigável. Sempre que for ativado, isso significa que o usuário acabou de registrar uma nova despesa. Sua única tarefa é responder com uma mensagem simpática e encorajadora confirmando o registro da despesa.\n" +
                "Responda apenas com uma mensagem amigável ao usuário confirmando o registro da despesa. Não forneça análises ou informações adicionais.\n" +
                "despesa registrada: " + newExpense.description() + " R$ " + newExpense.amount() + " na categoria: " + categoryName

        ;
        var request = instanceAnswersForUser(message);

        HttpEntity<OpenAiMessageRequest> requestHttpEntity = new HttpEntity<>(request);

        var openAiResponse = restTemplate.exchange(
                OPENAI_URL,
                HttpMethod.POST,
                requestHttpEntity,
                OpenAiMessageResponse.class
        ).getBody();

        log.info("OpenAiResponse: {}", openAiResponse);

        return new ChatbotMessage(openAiResponse.output().get(0).content().get(0).text());
    }

    public ChatbotMessage newCategoryMessage(CategoryDTO categoryName) {
        var message = "Você é Monify, um assistente financeiro amigável. Sempre que for ativado, isso significa que o usuário acabou de registrar uma nova categoria. Sua única tarefa é responder com uma mensagem simpática e encorajadora confirmando o registro da categoria.\n" +
                "Responda apenas com uma mensagem amigável ao usuário confirmando o registro da despesa. Não forneça análises ou informações adicionais.\n" +
                "categoria registrada: " + categoryName

                ;
        var request = instanceAnswersForUser(message);

        HttpEntity<OpenAiMessageRequest> requestHttpEntity = new HttpEntity<>(request);

        var openAiResponse = restTemplate.exchange(
                OPENAI_URL,
                HttpMethod.POST,
                requestHttpEntity,
                OpenAiMessageResponse.class
        ).getBody();

        log.info("OpenAiResponse: {}", openAiResponse);

        return new ChatbotMessage(openAiResponse.output().get(0).content().get(0).text());
    }

    public ChatbotMessage deleteCategoryMessage(CategoryDTO categoryName) {
        var message = "Você é Monify, um assistente financeiro amigável. Sempre que for ativado, isso significa que o usuário acabou de excluir uma categoria. Sua única tarefa é responder com uma mensagem simpática e encorajadora confirmando o a exclusao da categoria.\n" +
                "Responda apenas com uma mensagem amigável ao usuário confirmando a exclusao da categoria. Não forneça análises ou informações adicionais.\n" +
                "categoria excluida: " + categoryName

                ;
        var request = instanceAnswersForUser(message);

        HttpEntity<OpenAiMessageRequest> requestHttpEntity = new HttpEntity<>(request);

        var openAiResponse = restTemplate.exchange(
                OPENAI_URL,
                HttpMethod.POST,
                requestHttpEntity,
                OpenAiMessageResponse.class
        ).getBody();

        log.info("OpenAiResponse: {}", openAiResponse);

        return new ChatbotMessage(openAiResponse.output().get(0).content().get(0).text());
    }

    private OpenAiMessageRequest instanceAnswersForUser(String systemMessage) {
        List<OpenAiMessageRequest.Message> allMessages = new ArrayList<>();
        var message = new OpenAiMessageRequest.Message("assistant", List.of(OpenAiMessageRequest.Content.fromRole(systemMessage, "assistant")));
        allMessages.add(message);

        return new OpenAiMessageRequest(
                "gpt-4.1",
                allMessages,
                null,
                new OpenAiMessageRequest.Text(new OpenAiMessageRequest.Format("text")),
                "auto"
            );
    }

}
