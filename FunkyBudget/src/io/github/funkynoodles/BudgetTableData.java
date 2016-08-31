package io.github.funkynoodles;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class BudgetTableData {

	private String generalCategory;
	private String scale;

	private Map<LocalDate, Double> actualMap = new HashMap<>();
	private Map<LocalDate, Double> budgetMap = new HashMap<>();

	public BudgetTableData(String generalCategory, String scale) {
		this.generalCategory = generalCategory;
		this.scale = scale;
	}

	/**
	 * Add a data point of this category,
	 * date will get sorted
	 * @param date
	 * @param number
	 */
	public void add(LocalDate date, double number) {
		switch (scale) {
		case Reference.BUDGET_TAB_SCALE_WEEKLY:
			addWeekly(date, number);
			break;
		case Reference.BUDGET_TAB_SCALE_MONTHLY:
			addMonthly(date, number);
			break;
		case Reference.BUDGET_TAB_SCALE_YEARLY:
			addYearly(date, number);
			break;
		default:
			addWeekly(date, number);
			break;
		}
	}

	private void addYearly(LocalDate date, double number) {
		LocalDate firstOfYear = LocalDate.of(date.getYear(), 1, 1);
		double oldValue = 0.0;
		if (actualMap.containsKey(firstOfYear)) {
			oldValue = actualMap.get(firstOfYear);
		}
		actualMap.put(firstOfYear, number + oldValue);
	}

	private void addMonthly(LocalDate date, double number) {
		LocalDate firstOfMonth = LocalDate.of(date.getYear(), date.getMonthValue(), 1);
		double oldValue = 0.0;
		if (actualMap.containsKey(firstOfMonth)) {
			oldValue = actualMap.get(firstOfMonth);
		}
		actualMap.put(firstOfMonth, number + oldValue);
	}

	private void addWeekly(LocalDate date, double number) {
		LocalDate sunday = date.minusDays(date.getDayOfWeek().getValue() % 7);
		double oldValue = 0.0;
		if (actualMap.containsKey(sunday)) {
			oldValue = actualMap.get(sunday);
		}
		actualMap.put(sunday, number + oldValue);
	}

	public double getDataDouble(LocalDate date){
		switch (scale) {
		case Reference.BUDGET_TAB_SCALE_WEEKLY:
			return getDataWeekDouble(date);
		case Reference.BUDGET_TAB_SCALE_MONTHLY:
			return getDataMonthDouble(date);
		case Reference.BUDGET_TAB_SCALE_YEARLY:
			return getDataYearDouble(date);
		default:
			return getDataWeekDouble(date);
		}
	}

	public String getData(LocalDate date) {
		switch (scale) {
		case Reference.BUDGET_TAB_SCALE_WEEKLY:
			return getDataWeek(date);
		case Reference.BUDGET_TAB_SCALE_MONTHLY:
			return getDataMonth(date);
		case Reference.BUDGET_TAB_SCALE_YEARLY:
			return getDataYear(date);
		default:
			return getDataWeek(date);
		}
	}

	private double getDataYearDouble(LocalDate date) {
		LocalDate firstOfYear = LocalDate.of(date.getYear(), 1, 1);
		double returnVal = 0.0;
		if (actualMap.containsKey(firstOfYear)) {
			returnVal = actualMap.get(firstOfYear).doubleValue();
		}
		return returnVal;
	}

	private String getDataYear(LocalDate date) {
		LocalDate firstOfYear = LocalDate.of(date.getYear(), 1, 1);
		String returnVal = "0.00";
		if (actualMap.containsKey(firstOfYear)) {
			returnVal = StringUtils.format2Decimals(actualMap.get(firstOfYear));
		}
		return returnVal;
	}

	private double getDataMonthDouble(LocalDate date) {
		LocalDate firstOfMonth = LocalDate.of(date.getYear(), date.getMonthValue(), 1);
		double returnVal = 0.0;
		if (actualMap.containsKey(firstOfMonth)) {
			returnVal = actualMap.get(firstOfMonth).doubleValue();
		}
		return returnVal;
	}

	private String getDataMonth(LocalDate date) {
		LocalDate firstOfMonth = LocalDate.of(date.getYear(), date.getMonthValue(), 1);
		String returnVal = "0.00";
		if (actualMap.containsKey(firstOfMonth)) {
			returnVal = StringUtils.format2Decimals(actualMap.get(firstOfMonth));
		}
		return returnVal;
	}

	private double getDataWeekDouble(LocalDate date) {
		LocalDate sunday = date.minusDays(date.getDayOfWeek().getValue() % 7);
		double returnVal = 0.0;
		if (actualMap.containsKey(sunday)) {
			returnVal = actualMap.get(sunday).doubleValue();
		}
		return returnVal;
	}

	private String getDataWeek(LocalDate date) {
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
