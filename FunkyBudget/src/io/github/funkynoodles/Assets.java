package io.github.funkynoodles;

import java.util.ArrayList;

public class Assets {
	private ArrayList<Asset> accountList = new ArrayList<Asset>();

	public int size(){
		return accountList.size();
	}

	public void insert(Asset account){
		accountList.add(account);
	}

	public ArrayList<Asset> getAccountList() {
		return accountList;
	}

	public void setAccountList(ArrayList<Asset> accountList) {
		this.accountList = accountList;
	}

}
