package lucastexiera.com.mschatbotopenai.dto.financemonify;

import java.math.BigDecimal;

public record ExpenseDTO(
        Long userId,
        String description,
        BigDecimal amount,
        Long category_id

) {
}
