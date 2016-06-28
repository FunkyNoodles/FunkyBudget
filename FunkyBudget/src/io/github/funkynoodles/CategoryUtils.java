package io.github.funkynoodles;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;

public class CategoryUtils {

	public static BiMap<Category, String> map = HashBiMap.create();

	public static void populateMap(){
		map.put(Category.EXPENSE_AUTO_FEES, "Expense:Auto:Fees");
		map.put(Category.EXPENSE_AUTO_GAS, "Expense:Auto:Gas");
		map.put(Category.EXPENSE_AUTO_MAINT, "Expense:Auto:Maintenance");
		map.put(Category.EXPENSE_AUTO_PARKING, "Expense:Auto:Parking");
		map.put(Category.EXPENSE_BANK_SERVICE, "Expense:Bank Services");
		map.put(Category.EXPENSE_BOOKS, "Expense:Books");
		map.put(Category.EXPENSE_CABLE, "Expense:Cable");
		map.put(Category.EXPENSE_CHARITY, "Expense:Charity");
		map.put(Category.EXPENSE_CLOTHES, "Expense:Clothes");
		map.put(Category.EXPENSE_COMPUTER, "Expense:Computer");
		map.put(Category.EXPENSE_DINGING, "Expense:Dining");
		map.put(Category.EXPENSE_EDUCATION, "Expense:Education");
		map.put(Category.EXPENSE_ENTER_MOVIES, "Expense:Entertainment:Movies");
		map.put(Category.EXPENSE_ENTER_MUSIC, "Expense:Entertainment:Music");
		map.put(Category.EXPENSE_ETNER_RECREATION, "Expense:Entertainment:Recreation");
		map.put(Category.EXPENSE_GIFTS, "Expense:Gifts");
		map.put(Category.EXPENSE_GROCERIES, "Expense:Groceries");
		map.put(Category.EXPENSE_HOBBIES, "Expense:Hobbies");
		map.put(Category.EXPENSE_INSURANCE, "Expense:Insurance");
		map.put(Category.EXPENSE_INSURANCE_AUTO, "Expense:Insurance:Auto");
		map.put(Category.EXPENSE_INSURANCE_HEALTH, "Expense:Insurance:Health");
		map.put(Category.EXPENSE_INSURANCE_LIFE, "Expense:Insurance:Life");
		map.put(Category.EXPENSE_LAUNDRY, "Expense:Laundry/Dry Cleaning");
		map.put(Category.EXPENSE_MEDICAL, "Expense:Medical");
		map.put(Category.EXPENSE_MISCELLANEOUS, "Expense:Miscellaneous");
		map.put(Category.EXPENSE_ONLINE_SERVICE, "Expense:Online Services");
		map.put(Category.EXPENSE_PHONE, "Expense:Phone");
		map.put(Category.EXPENSE_PUBLIC_TRANSPORTATION, "Expense:Public Transportation");
		map.put(Category.EXPENSE_SUBSCRIPTIONS, "Expense:Subscriptions");
		map.put(Category.EXPENSE_SUPPLIES, "Expense:Supplies");
		map.put(Category.EXPENSE_TAXES, "Expense:Taxes");
		map.put(Category.EXPENSE_TRAVEL, "Expense:Travel");
		map.put(Category.EXPENSE_TRAVEL_HOTEL, "Expense:Travel:Hotel");
		map.put(Category.EXPENSE_TRAVEL_SOUVENIR, "Expense:Travel:Souvenir");
		map.put(Category.EXPENSE_TRAVEL_TICKETS, "Expense:Travel:Tickets");
		map.put(Category.EXPENSE_TRAVEL_TRANSPORTATION, "Expense:Travel:Transportation");
		map.put(Category.EXPENSE_UTILLITIES, "Expense:Utillities");
		map.put(Category.EXPENSE_UTILLITIES_ELECTRIC, "Expense:Utillities:Electric");
		map.put(Category.EXPENSE_UTILLITIES_GARBAGE, "Expense:Utillities:Garbage");
		map.put(Category.EXPENSE_UTILLITIES_GAS, "Expense:Utillities:Gas");
		map.put(Category.EXPENSE_UTILLITIES_WATER, "Expense:Utillities:Water");
		map.put(Category.INCOME_BONUS, "Income:Bonus");
		map.put(Category.INCOME_GIFTS, "Income:Gifts");
		map.put(Category.INCOME_INTEREST, "Income:Interest");
		map.put(Category.INCOME_OTHER, "Income:Other");
		map.put(Category.INCOME_SALARY, "Income:Salary");
	}

	public static void populateCategoryChoiceBox(ComboBox<String> comboBox, Asset a){
		//TODO: add assets' saved categories
		ObservableList<String> list = FXCollections.observableArrayList();
		for(Category c : Category.values()){
			list.add(map.get(c));
		}
		comboBox.setItems(list);
	}
}
