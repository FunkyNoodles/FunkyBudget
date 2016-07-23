package io.github.funkynoodles;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.layout.VBox;

public class FXMLBudgetTabController {

	@FXML private TreeTableView<BudgetTableData> treeTableView;
	@FXML private TreeTableColumn<BudgetTableData, String> firstColumn;

	@FXML private ComboBox<String> periodComboBox;
	@FXML private ComboBox<String> scaleComboBox;

	final private TreeItem<BudgetTableData> root = new TreeItem<>(new BudgetTableData("Budget Report"));
	private TreeItem<BudgetTableData> assetItems  = new TreeItem<>();
	private TreeItem<BudgetTableData> incomeItems  = new TreeItem<>();
	private TreeItem<BudgetTableData> expenseItems  = new TreeItem<>();

	private List<BudgetTableData> incomeList = new ArrayList<>();
	private List<BudgetTableData> expenseList = new ArrayList<>();

	private void createBudgetTableData(String period, String scale){
		// Get correct dates
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

		// Map all categories to data
		Map<String, BudgetTableData> incomeMap = new TreeMap<>();
		Map<String, BudgetTableData> expenseMap = new TreeMap<>();
		for (Category c : Category.values()) {
			String cStr = EnumUtils.categoryMap.get(c);
			if (cStr.contains("Income:")) {
				String incomeStr = EnumUtils.getGeneralCategory(cStr);
				if (!incomeMap.containsKey(incomeStr)) {
					incomeMap.put(incomeStr, new BudgetTableData(incomeStr));
				}
			}else if (cStr.contains("Expense:")) {
				String expenseStr = EnumUtils.getGeneralCategory(cStr);
				if (!expenseMap.containsKey(expenseStr)) {
					expenseMap.put(expenseStr, new BudgetTableData(expenseStr));
				}
			}
		}
		// Populate data points
		for (Asset asset : Main.assets.getAssetsList()) {
			for (TransferField tf : asset.getTransferField()) {
				if (DateUtils.isBetweenDatesInclusive(tf.getDate(), fromDate, nowDate)) {
					String cStr = tf.getCategoryStr();
					if (cStr.contains("Income:")) {
						String gcStr = EnumUtils.getGeneralCategory(cStr);
						incomeMap.get(gcStr).add(tf.getDate(), tf.getAmount());
					}else if (cStr.contains("Expense:")) {
						String gcStr = EnumUtils.getGeneralCategory(cStr);
						expenseMap.get(gcStr).add(tf.getDate(), tf.getAmount());
					}
				}
			}
		}

		// Copy to lists
		for (String cStr : incomeMap.keySet()) {
			incomeList.add(incomeMap.get(cStr));
		}
		for (String cStr : expenseMap.keySet()) {
			expenseList.add(expenseMap.get(cStr));
		}
		incomeList.stream().forEach((data) -> {
			incomeItems.getChildren().add(new TreeItem<>(data));
		});
		expenseList.stream().forEach((data) -> {
			expenseItems.getChildren().add(new TreeItem<>(data));
		});

		// Sum up values for parent nodes
		BudgetTableData incomeData = new BudgetTableData("Income");
		BudgetTableData expensData = new BudgetTableData("Expense");


		// Sum up assets
		BudgetTableData assetData = new BudgetTableData("Assets");
		for (Asset asset : Main.assets.getAssetsList()) {
			BudgetTableData data = new BudgetTableData(asset.getName());
			// Populate data
			for (TransferField tf : asset.getTransferField()) {
				if (DateUtils.isBetweenDatesInclusive(tf.getDate(), fromDate, nowDate)) {
					data.add(tf.getDate(), tf.getAmount());
				}
			}
			assetItems.getChildren().add(new TreeItem<>(data));
		}

		for (LocalDate weekIter = fromDate; weekIter.isBefore(nowDate); weekIter = weekIter.plusWeeks(1)) {
			// Asset items
			for (TreeItem<BudgetTableData> item : assetItems.getChildren()) {
				BudgetTableData data = item.getValue();
				double amount = data.getDataWeekDouble(weekIter);
				if (amount == 0.0) {
					assetData.add(weekIter, amount);
				}
			}
			// Income items
			for (TreeItem<BudgetTableData> item : incomeItems.getChildren()) {
				BudgetTableData data = item.getValue();
				double amount = data.getDataWeekDouble(weekIter);
				if (amount != 0.0) {
					incomeData.add(weekIter, amount);
				}
			}
			// Expense items
			for (TreeItem<BudgetTableData> item : expenseItems.getChildren()) {
				BudgetTableData data = item.getValue();
				double amount = data.getDataWeekDouble(weekIter);
				if (amount != 0.0) {
					expensData.add(weekIter, amount);
				}
			}
		}

		incomeItems.setValue(incomeData);
		expenseItems.setValue(expensData);
		assetItems.setValue(assetData);

		// Populate table
		treeTableView.setRoot(root);
		firstColumn.setCellValueFactory(
				(TreeTableColumn.CellDataFeatures<BudgetTableData, String> param) ->
        		new ReadOnlyStringWrapper(param.getValue().getValue().getCategory())
				);
		LocalDate weekIter = fromDate;
		while (weekIter.isBefore(nowDate)) {
			TreeTableColumn<BudgetTableData, String> newCol= new TreeTableColumn<>(weekIter.plusDays(3).toString());
			LocalDate curWeek = weekIter;
            newCol.setCellValueFactory(
            		(TreeTableColumn.CellDataFeatures<BudgetTableData, String> param) ->
            		new ReadOnlyStringWrapper(param.getValue().getValue().getDataWeek(curWeek))
            );
            treeTableView.getColumns().add(newCol);
            weekIter = weekIter.plusWeeks(1);
		}
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

		root.setExpanded(true);
		assetItems.setExpanded(true);
		incomeItems.setExpanded(true);
		expenseItems.setExpanded(true);

		root.getChildren().add(assetItems);
		root.getChildren().add(incomeItems);
		root.getChildren().add(expenseItems);

		treeTableView.prefHeightProperty().bind(((VBox)treeTableView.getParent()).prefHeightProperty());

		createBudgetTableData(Reference.BUDGET_TAB_PAST_12_WEEKS, Reference.BUDGET_TAB_SCALE_WEEKLY);
	}
}
