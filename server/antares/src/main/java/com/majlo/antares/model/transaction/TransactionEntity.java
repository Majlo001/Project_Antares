package com.majlo.antares.model.transaction;

import com.majlo.antares.model.User;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Builder
@Table(name = "transaction")
public class TransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionNumber;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "transactionEntity", cascade = CascadeType.ALL)
    private List<TransactionEntityItem> transactionEntityItems;

    private double totalAmount;

    private LocalDateTime transactionDate;


    // TODO: Create a separate table for discounts codes
//    private String discountCode;
//    private double discountAmount;

    // TODO: Create a separate table for payment methods (eg. "PayPal", "Credit Card", "Blik")
//    private String paymentMethod;

    // TODO: Create a separate table for transaction statuses
//    private String status;  // Status transakcji (np. SUCCESS, FAILED)
}
