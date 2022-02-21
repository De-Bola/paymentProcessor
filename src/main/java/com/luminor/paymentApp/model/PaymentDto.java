package com.luminor.paymentApp.model;

public class PaymentDto {
    private String amount;

    private String debtorIban;

    public PaymentDto() {}

    public PaymentDto(String amount, String debtorIban) {
        this.amount = amount;
        this.debtorIban = debtorIban;
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

    @Override
    public String toString() {
        return "PaymentDto{" +
                "amount='" + amount + '\'' +
                ", iban='" + debtorIban + '\'' +
                '}';
    }
}
