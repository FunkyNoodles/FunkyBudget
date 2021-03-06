package io.github.funkynoodles;

import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDate;
import java.util.Comparator;

public class TransferField implements Comparable<TransferField>, Comparator<TransferField> {
    // This class holds data for each money transaction
    private LocalDate date;
    private String detail;
    private double amount;
    private Category category;
    private double currentBalance = 0;

    private SimpleStringProperty dateStr = new SimpleStringProperty();
    private SimpleStringProperty detailStr = new SimpleStringProperty();
    private SimpleStringProperty categoryStr = new SimpleStringProperty();
    private SimpleStringProperty amountStr = new SimpleStringProperty();
    private SimpleStringProperty balanceStr = new SimpleStringProperty();

    public TransferField(LocalDate d, double amountNum, String detail, Category cat) {
        setTransferField(d, amountNum, detail, cat, 0.0);
    }

    public TransferField(SaveTransferField stf) {
        setTransferField(stf.getDate(), stf.getAmount(), stf.getDetail(), stf.getCategory(), stf.getCurrentBalance());
    }

    private void setTransferField(LocalDate d, double amountNum, String detail, Category cat, double cb) {
        date = d;
        dateStr.setValue(date.toString());
        this.detail = detail;
        detailStr.setValue(detail);
        this.setAmount(amountNum);
        amountStr.setValue(StringUtils.format2Decimals(amountNum));
        setCategory(cat);
        categoryStr.setValue(EnumUtils.categoryMap.get(cat));
        setCurrentBalance(cb);
        balanceStr.setValue(getBalanceStr());
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getDateStr() {
        return dateStr.get();
    }

    public void setDateStr(SimpleStringProperty dateStr) {
        this.dateStr = dateStr;
    }

    public String getDetailStr() {
        return detailStr.get();
    }

    public void setDetailStr(SimpleStringProperty detailStr) {
        this.detailStr = detailStr;
    }

    public String getCategoryStr() {
        return categoryStr.get();
    }

    public void setCategoryStr(SimpleStringProperty categoryStr) {
        this.categoryStr = categoryStr;
    }

    public double getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(double currentBalance) {
        this.currentBalance = currentBalance;
    }

    public String getBalanceStr() {
        String str = Double.toString(currentBalance);
        return StringUtils.format2Decimals(str);
    }

    @Override
    public int compareTo(TransferField o) {
        return date.toString().compareTo(o.getDateStr());
    }

    @Override
    public int compare(TransferField o1, TransferField o2) {
        return o1.compareTo(o2);
    }

    public String getAmountStr() {
        String str = amountStr.get();
        return StringUtils.format2Decimals(str);
    }

    public void setAmountStr(SimpleStringProperty amountStr) {
        this.amountStr = amountStr;
    }
}
