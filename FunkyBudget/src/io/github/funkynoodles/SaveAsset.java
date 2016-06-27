package io.github.funkynoodles;

import java.util.ArrayList;

// With truncated information to save
public class SaveAsset {
	private String name;
	private AssetType type;
	private double balance = 0;
	private ArrayList<SaveTransferField> transferField = new ArrayList<SaveTransferField>();

	public SaveAsset(String n, AssetType t, double b, ArrayList<TransferField> arr){
		setName(n);
		setType(t);
		setBalance(b);
		for(int i = 0; i < arr.size(); i++){
			transferField.add(new SaveTransferField(arr.get(i).getDate(), arr.get(i).getAmount(), arr.get(i).getDetail(), arr.get(i).getCategory(), arr.get(i).getCurrentBalance()));
		}
	}

	public ArrayList<SaveTransferField> getTransferField() {
		return transferField;
	}

	public void setTransferField(ArrayList<SaveTransferField> transferField) {
		this.transferField = transferField;
	}

	public SaveAsset(Asset a){
		setName(a.getName());
		setType(a.getType());
		setBalance(a.getBalance());
		for(int i = 0; i < a.getTransferField().size(); i++){
			transferField.add(new SaveTransferField(a.getTransferField().get(i)));
		}
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

	public void setBalance(double balance) {
		this.balance = balance;
	}
}
