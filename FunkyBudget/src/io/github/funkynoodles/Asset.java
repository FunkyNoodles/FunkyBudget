package io.github.funkynoodles;

import java.util.ArrayList;
import java.util.Collections;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Asset {
	// Asset class
	private String name;
	private AssetType type;
	private double balance = 0;
	private ArrayList<TransferField> transferField = new ArrayList<TransferField>();
	private ObservableList<TransferField> observableTransferField = FXCollections.observableArrayList();

	public Asset(){
		balance = 0;
	}

	public Asset(String name, AssetType type){
		this.name = name;
		this.type = type;
		balance = 0;
	}

	public Asset(SaveAsset sa){
		name = sa.getName();
		type = sa.getType();
		balance = sa.getBalance();
		transferField.clear();
		observableTransferField.clear();
		for (int i = 0; i < sa.getTransferField().size(); i++) {
			transferField.add(new TransferField(sa.getTransferField().get(i)));
		}
		observableTransferField = FXCollections.observableArrayList(transferField);
	}

	public int size(){
		return transferField.size();
	}

	public void insert(TransferField tf){
		// Insert in order of date
		if (transferField.size() == 0) {
			tf.setCurrentBalance(tf.getAmount());
			transferField.add(tf);
			observableTransferField.add(tf);
			return;
		}else{
			double newBalance = transferField.get(transferField.size() - 1).getCurrentBalance() + tf.getAmount();
			System.out.println("NB: " + newBalance);
			tf.setCurrentBalance(newBalance);
			for (int i = 0; i < transferField.size(); i++) {
				if (!tf.getDate().isBefore(transferField.get(i).getDate())) {
					continue;
				}else{
					transferField.add(i ,tf);
					observableTransferField.add(i, tf);
					return;
				}
			}
		}
		transferField.add(tf);
		observableTransferField.add(tf);
	}

	public void sort(){
		Collections.sort(transferField);
		Collections.sort(observableTransferField);
	}

	public void updateBalance(){
		TransferField tf = null;
		if (transferField.size() == 0){
			return;
		}
		for (int i = 0; i < transferField.size(); i++) {
			tf = transferField.get(i);
			if (i == 0) {
				tf.setCurrentBalance(tf.getAmount());
			}else{
				TransferField prevTf = transferField.get(i - 1);
				tf.setCurrentBalance(prevTf.getCurrentBalance() + tf.getAmount());
			}
		}
		observableTransferField = FXCollections.observableArrayList(transferField);
		balance = tf.getCurrentBalance();
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
		return StringUtils.format2Decimals(str);
	}
	public void setBalance(double balance) {
		this.balance = balance;
	}
	public void addBalance(int amount){
		this.balance += amount;
	}
	public ArrayList<TransferField> getTransferField() {
		return transferField;
	}
	public void setTransferField(ArrayList<TransferField> transferField) {
		this.transferField = transferField;
	}
	public ObservableList<TransferField> getObservableTransferField() {
		return observableTransferField;
	}
	public void setObservableTransferField(ObservableList<TransferField> observableTransferField) {
		this.observableTransferField = observableTransferField;
	}
}
