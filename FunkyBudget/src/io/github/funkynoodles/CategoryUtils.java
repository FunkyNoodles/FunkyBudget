package io.github.funkynoodles;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ChoiceBox;

public class CategoryUtils {
	public static String toString(Category c){
		String str = "Default";
		if(c == Category.EXPENSE_AUTO){
			return "Expense: Auto";
		}
		return str;
	}

	public static Category toCategory(String s){
		if(s == "Expense: Auto"){
			return Category.EXPENSE_AUTO;
		}
		return null;
	}

	public static void populateCategoryChoiceBox(ChoiceBox<String> choiceBox, Asset a){
		//TODO: add assets' saved categories
		ObservableList<String> list = FXCollections.observableArrayList();
		for(Category c : Category.values()){
			list.add(toString(c));
		}
		choiceBox.setItems(list);
	}
}
