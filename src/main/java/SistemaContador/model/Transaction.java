package SistemaContador.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private Integer transactionId;

    @Column(name = "client_id")
    private Integer clientId;

    @Transient
    private String clientName;

    @Column(name = "category_id")
    private Integer categoryId;

    @Transient
    private String categoryName;

    private String type;

    private Double amount;

    @Column(name = "transaction_date")
    private LocalDate transactionDate;

    private String description;
}