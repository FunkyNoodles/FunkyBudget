package io.github.funkynoodles;

import java.util.ArrayList;

public class Account {
	// Account class
	private String name;
	private AccountType type;
	public ArrayList<TransferField> tran = new ArrayList<TransferField>();

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public AccountType getType() {
		return type;
	}
	public void setType(AccountType type) {
		this.type = type;
	}
}
