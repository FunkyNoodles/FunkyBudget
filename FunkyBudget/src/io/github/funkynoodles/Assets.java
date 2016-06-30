package io.github.funkynoodles;

import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Assets {
	private ArrayList<Asset> assetList = new ArrayList<Asset>();
	private ObservableList<String> assetObservableList = FXCollections.observableArrayList();

	public int size(){
		return assetList.size();
	}

	public void insert(Asset a){
		assetList.add(a);
		assetObservableList.add(a.getName());
	}

	public ArrayList<Asset> getAssetsList() {
		return assetList;
	}

	public void setAssetsList(ArrayList<Asset> accountList) {
		this.assetList = accountList;
	}

	public void loadFromSave(SaveAssets sa){
		for(int i = 0; i < sa.size(); i ++){
			assetList.add(new Asset(sa.getAssetsList().get(i)));
		}
	}

	public ObservableList<String> getAssetObservableList(){
		return assetObservableList;
	}
}
