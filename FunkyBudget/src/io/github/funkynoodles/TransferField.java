package io.github.funkynoodles;

import java.time.LocalDate;

public class TransferField {
	// This class holds data for each money transaction
	private LocalDate date;


	private double amount;
	private String detail;
	private Category category;

	public TransferField(LocalDate d, int amount, String detail, Category cat){
		date = d;
		this.setAmount(amount);
		this.detail = detail;
		setCategory(cat);
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
}
