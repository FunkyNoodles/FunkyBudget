package io.github.funkynoodles;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class BudgetTableData {

	private String generalCategory;

	private Map<LocalDate, Double> actualMap = new HashMap<>();
	private Map<LocalDate, Double> budgetMap = new HashMap<>();

	private LocalDate fromDate, toDate;

	public BudgetTableData(String generalCategory) {
		this.generalCategory = generalCategory;
	}

	/**
	 * Add a data point of this category
	 * date will get sorted to the week
	 * @param date
	 * @param number
	 */
	public void add(LocalDate date, double number) {
		LocalDate sunday = date.minusDays(date.getDayOfWeek().getValue() % 7);
		double oldValue = 0.0;
		if (actualMap.containsKey(sunday)) {
			oldValue = actualMap.get(sunday);
		}
		actualMap.put(sunday, number + oldValue);
	}

	public String getDataWeek(LocalDate date) {
		LocalDate sunday = date.minusDays(date.getDayOfWeek().getValue() % 7);
		String returnVal = "0.00";
		if (actualMap.containsKey(sunday)) {
			returnVal = StringUtils.format2Decimals(actualMap.get(sunday));
		}
		return returnVal;
	}

	public String getCategory(){
		return generalCategory;
	}
}
