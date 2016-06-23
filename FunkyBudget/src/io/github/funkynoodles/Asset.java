package io.github.funkynoodles;

import java.util.ArrayList;

public class Asset {
	// Asset class
	private String name;
	private AssetType type;
	private int balance = 0;
	public ArrayList<TransferField> tran = new ArrayList<TransferField>();

	public Asset(){
		balance = 0;
	}

	public Asset(String name, AssetType type){
		this.name = name;
		this.type = type;
		balance = 0;
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
	public int getBalance() {
		return balance;
	}
	public void setBalance(int balance) {
		this.balance = balance;
	}
	public void addBalance(int amount){
		this.balance += amount;
	}
}
