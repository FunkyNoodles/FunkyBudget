package io.github.funkynoodles;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;

public class FXMLBudgetTabController {

	@FXML private TreeTableView<BudgetTableData> treeTableView;
	@FXML private TreeTableColumn<BudgetTableData, String> firstColumn;

	@FXML private ComboBox<String> periodComboBox;
	@FXML private ComboBox<String> scaleComboBox;

	private TreeItem<BudgetTableData> assetItems  = new TreeItem<>();
	private TreeItem<BudgetTableData> incomeItems  = new TreeItem<>();
	private TreeItem<BudgetTableData> expenseItems  = new TreeItem<>();

	private List<BudgetTableData> assetList;
	private List<BudgetTableData> incomeList;
	private List<BudgetTableData> expenseList;

	private void createBudgetTableData(String period, String scale){
		LocalDate nowDate = LocalDate.now();
		LocalDate fromDate;
		switch (period) {
		case Reference.BUDGET_TAB_PAST_12_WEEKS:
			fromDate = nowDate.minusWeeks(12);
			break;
		case Reference.BUDGET_TAB_PAST_24_WEEKS:
			fromDate = nowDate.minusWeeks(24);
			break;
		case Reference.BUDGET_TAB_PAST_48_WEEKS:
			fromDate = nowDate.minusWeeks(48);
			break;
		case Reference.BUDGET_TAB_PAST_YEAR:
			fromDate = nowDate.minusYears(1);
			break;
		case Reference.BUDGET_TAB_PAST_2_YEARS:
			fromDate = nowDate.minusYears(2);
		default:
			fromDate = nowDate.minusWeeks(12);
			break;
		}
		// Refine start of the period to a sunday
		DayOfWeek week = fromDate.getDayOfWeek();
		fromDate = fromDate.minusDays(week.getValue() % 7);
	}

	@FXML
	public void initialize(){
		ObservableList<String> periodList = FXCollections.observableArrayList(
				Reference.BUDGET_TAB_PAST_12_WEEKS,
				Reference.BUDGET_TAB_PAST_24_WEEKS,
				Reference.BUDGET_TAB_PAST_48_WEEKS,
				Reference.BUDGET_TAB_PAST_YEAR,
				Reference.BUDGET_TAB_PAST_2_YEARS);
		periodComboBox.setItems(periodList);
		periodComboBox.setValue(Reference.BUDGET_TAB_PAST_12_WEEKS);
		ObservableList<String> scaleList = FXCollections.observableArrayList(
				Reference.BUDGET_TAB_SCALE_WEEKLY,
				Reference.BUDGET_TAB_SCALE_MONTHLY,
				Reference.BUDGET_TAB_SCALE_YEARLY);
		scaleComboBox.setItems(scaleList);
		scaleComboBox.setValue(Reference.BUDGET_TAB_SCALE_WEEKLY);
	}
}
