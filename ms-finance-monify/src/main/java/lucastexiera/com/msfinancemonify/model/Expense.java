package lucastexiera.com.msfinancemonify.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lucastexiera.com.msfinancemonify.dto.ExpenseDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;
    private BigDecimal amount;
    @ManyToOne
    private Category category;
    private LocalDateTime createdAt = LocalDateTime.now();
    private Long userId;


    public Expense(Long id ,String description, BigDecimal amount, String category, Long userId) {
        this.description = description;
        this.amount = amount;
        this.category.setName(category);

        this.userId = userId;
    }

    public void updateFromDTO(ExpenseDTO dto, Category category) {
        this.description = dto.description();
        this.amount = dto.amount();
        this.category = category;
    }

}
