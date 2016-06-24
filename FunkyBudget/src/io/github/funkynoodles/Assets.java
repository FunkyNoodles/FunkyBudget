package io.github.funkynoodles;

import java.util.ArrayList;

public class Assets {
	private ArrayList<Asset> assettList = new ArrayList<Asset>();

	public int size(){
		return assettList.size();
	}

	public void insert(Asset account){
		assettList.add(account);
	}

	public ArrayList<Asset> getAssetsList() {
		return assettList;
	}

	public void setAssetsList(ArrayList<Asset> accountList) {
		this.assettList = accountList;
	}

}
