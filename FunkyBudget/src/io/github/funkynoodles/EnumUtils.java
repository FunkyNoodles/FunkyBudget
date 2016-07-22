package io.github.funkynoodles;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;

public class EnumUtils {

	public static BiMap<Category, String> categoryMap = HashBiMap.create();

	public static BiMap<AssetType, String> assetTypeMap = HashBiMap.create();

	public static void populateCategoryMap(){
		categoryMap.put(Category.EXPENSE_AUTO_FEES, "Expense:Auto:Fees");
		categoryMap.put(Category.EXPENSE_AUTO_GAS, "Expense:Auto:Gas");
		categoryMap.put(Category.EXPENSE_AUTO_MAINT, "Expense:Auto:Maintenance");
		categoryMap.put(Category.EXPENSE_AUTO_PARKING, "Expense:Auto:Parking");
		categoryMap.put(Category.EXPENSE_BANK_SERVICE, "Expense:Bank Services");
		categoryMap.put(Category.EXPENSE_BOOKS, "Expense:Books");
		categoryMap.put(Category.EXPENSE_CABLE, "Expense:Cable");
		categoryMap.put(Category.EXPENSE_CHARITY, "Expense:Charity");
		categoryMap.put(Category.EXPENSE_CLOTHES, "Expense:Clothes");
		categoryMap.put(Category.EXPENSE_COMPUTER, "Expense:Computer");
		categoryMap.put(Category.EXPENSE_DINGING, "Expense:Dining");
		categoryMap.put(Category.EXPENSE_EDUCATION, "Expense:Education");
		categoryMap.put(Category.EXPENSE_ENTER_MOVIES, "Expense:Entertainment:Movies");
		categoryMap.put(Category.EXPENSE_ENTER_MUSIC, "Expense:Entertainment:Music");
		categoryMap.put(Category.EXPENSE_ETNER_RECREATION, "Expense:Entertainment:Recreation");
		categoryMap.put(Category.EXPENSE_GIFTS, "Expense:Gifts");
		categoryMap.put(Category.EXPENSE_GROCERIES, "Expense:Groceries");
		categoryMap.put(Category.EXPENSE_HOBBIES, "Expense:Hobbies");
		categoryMap.put(Category.EXPENSE_INSURANCE, "Expense:Insurance");
		categoryMap.put(Category.EXPENSE_INSURANCE_AUTO, "Expense:Insurance:Auto");
		categoryMap.put(Category.EXPENSE_INSURANCE_HEALTH, "Expense:Insurance:Health");
		categoryMap.put(Category.EXPENSE_INSURANCE_LIFE, "Expense:Insurance:Life");
		categoryMap.put(Category.EXPENSE_LAUNDRY, "Expense:Laundry/Dry Cleaning");
		categoryMap.put(Category.EXPENSE_MEDICAL, "Expense:Medical");
		categoryMap.put(Category.EXPENSE_MISCELLANEOUS, "Expense:Miscellaneous");
		categoryMap.put(Category.EXPENSE_ONLINE_SERVICE, "Expense:Online Services");
		categoryMap.put(Category.EXPENSE_PHONE, "Expense:Phone");
		categoryMap.put(Category.EXPENSE_PUBLIC_TRANSPORTATION, "Expense:Public Transportation");
		categoryMap.put(Category.EXPENSE_SUBSCRIPTIONS, "Expense:Subscriptions");
		categoryMap.put(Category.EXPENSE_SUPPLIES, "Expense:Supplies");
		categoryMap.put(Category.EXPENSE_TAXES, "Expense:Taxes");
		categoryMap.put(Category.EXPENSE_TRAVEL, "Expense:Travel");
		categoryMap.put(Category.EXPENSE_TRAVEL_HOTEL, "Expense:Travel:Hotel");
		categoryMap.put(Category.EXPENSE_TRAVEL_SOUVENIR, "Expense:Travel:Souvenir");
		categoryMap.put(Category.EXPENSE_TRAVEL_TICKETS, "Expense:Travel:Tickets");
		categoryMap.put(Category.EXPENSE_TRAVEL_TRANSPORTATION, "Expense:Travel:Transportation");
		categoryMap.put(Category.EXPENSE_UTILLITIES, "Expense:Utillities");
		categoryMap.put(Category.EXPENSE_UTILLITIES_ELECTRIC, "Expense:Utillities:Electric");
		categoryMap.put(Category.EXPENSE_UTILLITIES_GARBAGE, "Expense:Utillities:Garbage");
		categoryMap.put(Category.EXPENSE_UTILLITIES_GAS, "Expense:Utillities:Gas");
		categoryMap.put(Category.EXPENSE_UTILLITIES_WATER, "Expense:Utillities:Water");
		categoryMap.put(Category.INCOME_BONUS, "Income:Bonus");
		categoryMap.put(Category.INCOME_GIFTS, "Income:Gifts");
		categoryMap.put(Category.INCOME_INTEREST, "Income:Interest");
		categoryMap.put(Category.INCOME_OTHER, "Income:Other");
		categoryMap.put(Category.INCOME_SALARY, "Income:Salary");
	}

	/**
	 * Ge general category,
	 * for example if the category is EXPENSE:AUTO:FEES
	 * the general category is AUTO
	 * @param c
	 * @return
	 */
	public static String getGeneralCategory(Category c) {
		String cat = categoryMap.get(c);
		return getGeneralCategory(cat);
	}

	public static String getGeneralCategory(String cat) {
		int firstColon = cat.indexOf(":");
		int secondColon = cat.indexOf(":", firstColon + 1);
		if (secondColon < 0) {
			return cat.substring(firstColon + 1);
		}else {
			return cat.substring(firstColon + 1, secondColon);
		}
	}

	public static void populateAssetTypeMap(){
		assetTypeMap.put(AssetType.BANK_CHECKINGS, "Bank Checkings");
		assetTypeMap.put(AssetType.BANK_SAVINGS, "Bank Savings");
		assetTypeMap.put(AssetType.CASH, "Cash");
	}

	public static void populateCategoryComboBox(ComboBox<String> comboBox, Asset a){
		populateCategoryComboBox(comboBox, a, 0);
	}

	/**
	 *
	 * @param comboBox
	 * @param a
	 * @param cmd 0 means all categories, 1 means only expenses, 2 means only income
	 */
	public static void populateCategoryComboBox(ComboBox<String> comboBox, Asset a, int cmd){
		ObservableList<String> list = FXCollections.observableArrayList();
		switch (cmd) {
		case 0:
			for(Category c : Category.values()){
				list.add(categoryMap.get(c));
			}
			break;
		case 1:
			for(Category c : Category.values()){
				String cStr = categoryMap.get(c);
				if (cStr.contains("Expense:")) {
					list.add(cStr);
				}
			}
		case 2:
			for(Category c : Category.values()){
				String cStr = categoryMap.get(c);
				if (cStr.contains("Income:")) {
					list.add(cStr);
				}
			}
		default:
			break;
		}
		comboBox.setItems(list);
	}

	public static void populateNewAssetTypeComboBox(ComboBox<String> comboBox){
		ObservableList<String> list = FXCollections.observableArrayList();
		for (AssetType type : AssetType.values()) {
			list.add(assetTypeMap.get(type));
		}
		comboBox.setItems(list);
	}
}
