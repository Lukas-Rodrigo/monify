package lucastexiera.com.mschatbotopenai.dto.chatbot;

import java.math.BigDecimal;

public record RegisterExpense(
        String description,
        BigDecimal amount,
        Long categoryId

) {
}
