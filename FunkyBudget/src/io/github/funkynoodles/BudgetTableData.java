package io.github.funkynoodles;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class BudgetTableData {

	private String generalCategory;

	private Map<LocalDate, Double> actualMap = new HashMap<>();
	private Map<LocalDate, Double> budgetMap = new HashMap<>();

	private LocalDate fromDate, toDate;

	public BudgetTableData(String generalCategory, LocalDate fromDate, LocalDate toDate, String scale) {
		this.generalCategory = generalCategory;
		this.fromDate = fromDate;
		this.toDate = toDate;
	}

	public String getCategory(){
		return generalCategory;
	}
}
