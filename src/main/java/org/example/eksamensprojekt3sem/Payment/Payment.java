package org.example.eksamensprojekt3sem.Payment;


import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import org.example.eksamensprojekt3sem.Enums.PaymentStatus;

import java.time.LocalDate;

@Entity
@Table(name = "payment")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long paymentId;

    @NotNull(message = "Membership must be specified")
    @ManyToOne
    @JoinColumn(name = "membership_id")
    private Membership membership;

    @NotNull(message = "Amount must be specified")
    @DecimalMin(value = "0.0", inclusive = false, message = "Amount must be greater than 0")
    @Column(name = "amount")
    private Double amount;

    @NotNull(message = "Payment date must be specified")
    @Column(name = "payment_date")
    private LocalDate paymentDate;

    @NotNull(message = "Status must be specified")
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private PaymentStatus status;

    @Override
    public String toString() {
        return "Payment{" +
                "paymentId=" + paymentId +
                ", membership=" + membership +
                ", amount=" + amount +
                ", paymentDate=" + paymentDate +
                ", status=" + status +
                '}';
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    public Membership getMembership() {
        return membership;
    }

    public void setMembership(Membership membership) {
        this.membership = membership;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }
}