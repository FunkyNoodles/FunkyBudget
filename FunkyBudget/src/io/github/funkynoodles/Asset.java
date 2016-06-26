package io.github.funkynoodles;

import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Asset {
	// Asset class
	private String name;
	private AssetType type;
	private double balance = 0;
	public ArrayList<TransferField> transferField = new ArrayList<TransferField>();
	public ObservableList<TransferField> observableTransferField = FXCollections.observableArrayList();

	public Asset(){
		balance = 0;
	}

	public Asset(String name, AssetType type){
		this.name = name;
		this.type = type;
		balance = 0;
	}

	public void insert(TransferField tf){
		if (transferField.size() == 0) {
			tf.setCurrentBalance(tf.getAmount());
		}else{
			tf.setCurrentBalance(transferField.get(transferField.size() - 1).getCurrentBalance() + tf.getAmount());
		}
		transferField.add(tf);
		observableTransferField.add(tf);
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public AssetType getType() {
		return type;
	}
	public void setType(AssetType type) {
		this.type = type;
	}
	public double getBalance() {
		return balance;
	}
	public String getBalanceStr(){
		String str = Double.toString(balance);
		int eIndex = str.indexOf("E");
		String eStr = "";
		if (eIndex != -1) {
			eStr = str.substring(eIndex);
		}
		int decimalIndex = str.indexOf(".");
		if (str.length() >= decimalIndex + 3) {
			str = str.substring(0,decimalIndex + 3) + eStr;
		}
		return str;
	}
	public void setBalance(double balance) {
		this.balance = balance;
	}
	public void addBalance(int amount){
		this.balance += amount;
	}

}
