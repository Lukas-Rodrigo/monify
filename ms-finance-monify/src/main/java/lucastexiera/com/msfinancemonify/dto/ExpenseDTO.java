package lucastexiera.com.msfinancemonify.dto;


import java.math.BigDecimal;

public record ExpenseDTO(
        String description,
        BigDecimal amount,
        Long category_id
) {
}
