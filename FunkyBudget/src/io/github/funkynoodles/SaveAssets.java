package io.github.funkynoodles;

import java.util.ArrayList;

public class SaveAssets {
	private String version = "";
	private ArrayList<SaveAsset> assetList = new ArrayList<SaveAsset>();

	public int size(){
		return assetList.size();
	}

	public void insert(SaveAsset account){
		assetList.add(account);
	}

	public ArrayList<SaveAsset> getAssetsList() {
		return assetList;
	}

	public void setAssetsList(ArrayList<SaveAsset> accountList) {
		this.assetList = accountList;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
}
