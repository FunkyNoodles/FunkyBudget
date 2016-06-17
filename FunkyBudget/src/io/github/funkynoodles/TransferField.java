package io.github.funkynoodles;

import java.util.ArrayList;

public class TransferField {
	// This class holds data for each money transaction
	private int year;
	private int month;
	private int day;
	private double amount;
	private String detail;
	public ArrayList<Category> categories = new ArrayList<Category>();

	public TransferField(int y, int m, int d, int amount, String detail){
		year = y;
		month = m;
		day = d;
		this.setAmount(amount);
		this.detail = detail;
	}

	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public int getMonth() {
		return month;
	}
	public void setMonth(int month) {
		month = month%13;
		this.month = month;
	}
	public int getDay() {
		return day;
	}
	public void setDay(int day) {
		day = day%32;
		this.day = day;
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
}
