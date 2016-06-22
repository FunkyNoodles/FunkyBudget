package io.github.funkynoodles;

import java.util.ArrayList;

public class Assets {
	private ArrayList<Account> accountList = new ArrayList<Account>();

	public int size(){
		return accountList.size();
	}

	public void insert(Account account){
		accountList.add(account);
	}

	public ArrayList<Account> getAccountList() {
		return accountList;
	}

	public void setAccountList(ArrayList<Account> accountList) {
		this.accountList = accountList;
	}

}
