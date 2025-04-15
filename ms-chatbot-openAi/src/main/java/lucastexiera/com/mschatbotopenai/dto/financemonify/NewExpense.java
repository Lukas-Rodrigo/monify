package lucastexiera.com.mschatbotopenai.dto.financemonify;

import java.math.BigDecimal;

public record NewExpense(
        String description,
        BigDecimal amount,
        Long category_id

) {
}
