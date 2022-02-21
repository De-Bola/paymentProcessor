package com.luminor.paymentApp.model;

import fr.marcwrobel.jbanking.iban.Iban;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table
public class Payment {
    @Id
    @GeneratedValue(generator = "UUID")
    @Column
    private UUID payment_id = UUID.randomUUID();

    @Column
    private BigDecimal amount;

    @Column
    private String debtorIban;

    @Column
    private String created_at;

    public Payment(){this.created_at = LocalDateTime.now().toString();}

    public UUID getPayment_id() {
        return payment_id;
    }

    public void setPayment_id() {
        this.payment_id = UUID.randomUUID();
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setAmount(String amount) {
        this.amount = BigDecimal.valueOf(Double.parseDouble(amount));
    }

    public String getIban() {
        return debtorIban;
    }

    public void setDebtorIban(String debtorIban) {
        this.debtorIban = debtorIban;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = LocalDateTime.now().toString();
    }

    @Override
    public String toString() {
        return "Payment{" +
                "payment_id=" + payment_id +
                ", amount=" + amount +
                ", iban='" + debtorIban + '\'' +
                ", created_at='" + created_at + '\'' +
                '}';
    }
}
