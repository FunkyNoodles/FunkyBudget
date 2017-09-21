package io.github.funkynoodles;

import java.time.LocalDate;

public class SaveTransferField {
    private LocalDate date;
    private String detail;
    private double amount;
    private Category category;
    private double currentBalance;

    public SaveTransferField(LocalDate d, double amount, String detail, Category c, double currentBalance) {
        setDate(d);
        this.setDetail(detail);
        this.setAmount(amount);
        setCategory(c);
        this.setCurrentBalance(currentBalance);
    }

    public SaveTransferField(TransferField tf) {
        setDate(tf.getDate());
        setDetail(tf.getDetail());
        setAmount(tf.getAmount());
        setCategory(tf.getCategory());
        setCurrentBalance(tf.getCurrentBalance());
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public double getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(double currentBalance) {
        this.currentBalance = currentBalance;
    }
}
