package lucastexiera.com.msfinancemonify.dto;

import java.math.BigDecimal;

public record CategoryPercentageDTO(
        String categoryName,
        BigDecimal percentage
) {
}
