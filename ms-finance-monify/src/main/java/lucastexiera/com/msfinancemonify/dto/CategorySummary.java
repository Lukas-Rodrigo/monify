package lucastexiera.com.msfinancemonify.dto;

import java.math.BigDecimal;

public record CategorySummary(
        String categoryName,
        BigDecimal totalCategoryExpense
) {
}
