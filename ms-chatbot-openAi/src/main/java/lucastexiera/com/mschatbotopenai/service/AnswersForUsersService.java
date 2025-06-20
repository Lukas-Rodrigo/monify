package lucastexiera.com.mschatbotopenai.service;

import java.lang.reflect.Array;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lucastexiera.com.mschatbotopenai.dto.chatbot.OpenAiMessageRequest;
import lucastexiera.com.mschatbotopenai.dto.chatbot.OpenAiMessageResponse;
import lucastexiera.com.mschatbotopenai.dto.chatbot.OpenAiRequestFactory;
import lucastexiera.com.mschatbotopenai.dto.financemonify.CategoryDTO;
import lucastexiera.com.mschatbotopenai.dto.financemonify.ExpenseDTO;
import lucastexiera.com.mschatbotopenai.dto.userwhatsapp.ChatbotMessage;

@Service
public class AnswersForUsersService {
    private static final Logger log = LoggerFactory.getLogger(AnswersForUsersService.class);

    private final RestTemplate restTemplate;
    private final String openAiUrl;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm");
    private static final NumberFormat CURRENCY_FORMAT = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

    private enum Action {
        NEW_EXPENSE,
        UPDATE_EXPENSE,
        NEW_CATEGORY,
        DELETE_CATEGORY
    }

    private static final Map<Action, String> TEMPLATES = new EnumMap<>(Action.class);
    static {
        TEMPLATES.put(Action.NEW_EXPENSE,
                "Você deve responder apenas com o bloco abaixo, sem explicações ou preâmbulos:\n" +
                        "✅ Despesa Adicionada com sucesso! \n\n" +
                        "📋 **Descrição:**  \n   {description}\n\n" +
                        "🕒 **Data/Hora:**  \n   {datetime}\n\n" +
                        "💰 **Valor:**  \n   R$ {amount}\n\n" +
                        "📂 **Categoria:**  \n   {category}");

        TEMPLATES.put(Action.UPDATE_EXPENSE,
                "Você deve responder apenas com o bloco abaixo, sem explicações ou preâmbulos: \n" +
                        "✅ Pronto, atualizamos sua despesa. \n \n" +
                        "📋 **Descrição Atualizada:**  \n   {description}\n\n" +
                        "🕒 **Data/Hora:**  \n   {datetime}\n\n" +
                        "💰 **Valor Atualizado:**  \n   R$ {amount}\n\n" +
                        "📂 **Categoria:**  \n   {category}");

        TEMPLATES.put(Action.NEW_CATEGORY,
                "Você deve responder apenas com o bloco abaixo, sem explicações ou preâmbulos:\n" +
                        "✅ Nova Categoria Adicionada! \n \n" +
                        "📂 **Categoria Registrada:**  \n   {category}\n\n" +
                        "🕒 **Data/Hora:**  \n   {datetime}");

        TEMPLATES.put(Action.DELETE_CATEGORY,
                "Você deve responder apenas com o bloco abaixo, sem explicações ou preâmbulos:\n" +
                        "Categoria excluida com sucesso\n \n" +
                        "📂 **Categoria Excluída:**  \n   {category}\n\n" +
                        "🕒 **Data/Hora:**  \n   {datetime}");
    }

    @Autowired
    public AnswersForUsersService(RestTemplate restTemplate,
            @Value("${openAi.api.baseURL}") String openAiUrl) {
        this.restTemplate = restTemplate;
        this.openAiUrl = openAiUrl;
    }

    public ChatbotMessage confirmNewExpense(ExpenseDTO expense, CategoryDTO category) {
        return sendConfirmation(Action.NEW_EXPENSE, mapParams(expense, category));
    }

    public ChatbotMessage confirmUpdateExpense(ExpenseDTO expense, CategoryDTO category) {
        return sendConfirmation(Action.UPDATE_EXPENSE, mapParams(expense, category));
    }

    public ChatbotMessage confirmNewCategory(CategoryDTO category) {
        Map<String, Object> params = Map.of(
                "category", category.name(),
                "datetime", formatDate());
        return sendConfirmation(Action.NEW_CATEGORY, params);
    }

    public ChatbotMessage confirmDeleteCategory(CategoryDTO category) {
        Map<String, Object> params = Map.of(
                "category", category.name(),
                "datetime", formatDate());
        return sendConfirmation(Action.DELETE_CATEGORY, params);
    }

    private ChatbotMessage sendConfirmation(Action action, Map<String, Object> params) {
        String template = TEMPLATES.get(action);
        String filled = fillTemplate(template, params);

        OpenAiMessageRequest request = buildSystemRequest(filled);
        OpenAiMessageResponse response = callOpenAi(request);

        String reply = response.choices().get(0).message().content();
        log.info("Generated confirmation [{}]: {}", action, reply);
        return new ChatbotMessage(reply);
    }

    private Map<String, Object> mapParams(ExpenseDTO expense, CategoryDTO category) {
        return Map.of(
                "description", formatString(expense.description()),
                "amount", formatCurrency(expense.amount()),
                "category", formatString(category.name()),
                "datetime", formatDate());
    }

    private String formatDate() {
        return LocalDateTime.now().format(FORMATTER);
    }

    private String fillTemplate(String template, Map<String, Object> params) {
        String result = template;
        for (var entry : params.entrySet()) {
            result = result.replace("{" + entry.getKey() + "}", entry.getValue().toString());
        }
        return result;
    }

    private OpenAiMessageRequest buildSystemRequest(String systemMessage) {
        var msg = new OpenAiMessageRequest.Message(
                "assistant",
                systemMessage);
        return new OpenAiMessageRequest(
                "gpt-4.1",
                List.of(msg),
                null,
                null);
    }

    private OpenAiMessageResponse callOpenAi(OpenAiMessageRequest request) {
        HttpEntity<OpenAiMessageRequest> entity = new HttpEntity<>(request);
        return restTemplate.exchange(
                openAiUrl,
                HttpMethod.POST,
                entity,
                OpenAiMessageResponse.class).getBody();
    }

    private String formatCurrency(Number value) {
        return CURRENCY_FORMAT.format(value);
    }

    private String formatString(String text) {
        if (text == null || text.isBlank())
            return text;
        return text.substring(0, 1).toUpperCase() + text.substring(1);
    }
}
