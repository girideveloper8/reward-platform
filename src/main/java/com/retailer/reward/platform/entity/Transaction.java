package com.retailer.reward.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;


/**
 * Entity class representing a transaction in the system.
 * Maps to the "transaction" table in the database.
 */
@Entity
@Table(name = "transaction")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private Long transactionId;

    @Column(nullable = false)
    private double amount;

    @Column(name = "transaction_month", nullable = false)
    private String transactionMonth;

    @ManyToOne()
    @JoinColumn(name = "customer_id", nullable = false) // Assuming 'customer_id' is the foreign key column
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @NotNull(message = "Customer cannot be null")
    private Customer customer;

}
