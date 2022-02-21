package com.luminor.paymentApp.model;

import java.util.UUID;

public class PaymentDao {

    private UUID payment_id;

    private String amount;

    private String debtorIban;

    private String created_at;

    public PaymentDao() {

    }

    public UUID getPayment_id() {
        return payment_id;
    }

    public void setPayment_id(UUID payment_id) {
        this.payment_id = payment_id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDebtorIban() {
        return debtorIban;
    }

    public void setDebtorIban(String debtorIban) {
        this.debtorIban = debtorIban;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    @Override
    public String toString() {
        return "PaymentDao{" +
                "payment_id='" + payment_id + '\'' +
                ", amount='" + amount + '\'' +
                ", debtorIban='" + debtorIban + '\'' +
                ", created_at='" + created_at + '\'' +
                '}';
    }
}
