package io.github.funkynoodles;

import java.util.ArrayList;

public class Assets {
	private ArrayList<Asset> assetList = new ArrayList<Asset>();

	public int size(){
		return assetList.size();
	}

	public void insert(Asset account){
		assetList.add(account);
	}

	public ArrayList<Asset> getAssetsList() {
		return assetList;
	}

	public void setAssetsList(ArrayList<Asset> accountList) {
		this.assetList = accountList;
	}
}
