package io.github.funkynoodles;

import java.time.LocalDate;

import javafx.beans.property.SimpleStringProperty;

public class TransferField {
	// This class holds data for each money transaction
	private LocalDate date;
	private String detail;
	private double amount;
	private Category category;
	private double currentBalance = 0;

	private SimpleStringProperty dateStr = new SimpleStringProperty();
	private SimpleStringProperty detailStr = new SimpleStringProperty();
	private SimpleStringProperty categoryStr = new SimpleStringProperty();
	private SimpleStringProperty depositStr = new SimpleStringProperty();
	private SimpleStringProperty withdrawlStr = new SimpleStringProperty();

	public TransferField(LocalDate d, double amountNum, String detail, Category cat){
		date = d;
		dateStr.setValue(date.toString());
		this.detail = detail;
		detailStr.setValue(detail);
		this.setAmount(amountNum);
		String amountString = Double.toString(amountNum);
		int decimalIndex = amountString.indexOf(".");
		if (amountString.length() >= decimalIndex + 3) {
			amountString = amountString.substring(0,decimalIndex + 3);
		}
		if(amountNum > 0){
			depositStr.setValue(amountString);
		}else if(amountNum < 0){
			withdrawlStr.setValue(amountString.substring(1));
		}else{
		}
		setCategory(cat);
		categoryStr.setValue(CategoryUtils.toString(cat));
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

	public String getDepositStr() {
		return depositStr.get();
	}

	public void setDepositStr(SimpleStringProperty depositStr) {
		this.depositStr = depositStr;
	}

	public String getWithdrawlStr() {
		return withdrawlStr.get();
	}

	public void setWithdrawlStr(SimpleStringProperty withdrawlStr) {
		this.withdrawlStr = withdrawlStr;
	}

	public double getCurrentBalance() {
		return currentBalance;
	}

	public void setCurrentBalance(double currentBalance) {
		this.currentBalance = currentBalance;
	}
}
