package lucastexiera.com.msfinancemonify.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private LocalDateTime CreatedAt;
    private Long userId;


    public Expense(Long id ,String description, BigDecimal amount, String category, LocalDateTime createdAt, Long userId) {
        this.description = description;
        this.amount = amount;
        this.category.setName(category);
        CreatedAt = createdAt;
        this.userId = userId;
    }

}
