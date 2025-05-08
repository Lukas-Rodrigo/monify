package lucastexiera.com.msfinancemonify.dto;

import java.math.BigDecimal;

public record ExpensesSummaryInPeriodDTO(
        String categoryName,
        BigDecimal totalCategoryExpense,
        BigDecimal totalExpenseInPeriod
) {
}
