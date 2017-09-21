package io.github.funkynoodles;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.layout.VBox;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class FXMLBudgetTabController {

    @FXML
    private TreeTableView<BudgetTableData> treeTableView;
    @FXML
    private TreeTableColumn<BudgetTableData, String> firstColumn;

    @FXML
    private ComboBox<String> periodComboBox;
    @FXML
    private ComboBox<String> scaleComboBox;

    private TreeItem<BudgetTableData> root = new TreeItem<>(new BudgetTableData("Budget Report", Reference.BUDGET_TAB_SCALE_WEEKLY));
    private TreeItem<BudgetTableData> assetItems = new TreeItem<>();
    private TreeItem<BudgetTableData> incomeItems = new TreeItem<>();
    private TreeItem<BudgetTableData> expenseItems = new TreeItem<>();

    private List<BudgetTableData> incomeList = new ArrayList<>();
    private List<BudgetTableData> expenseList = new ArrayList<>();

    private void createBudgetTableData(String period, String scale) {

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
            case Reference.BUDGET_TAB_PAST_YEAR:
                fromDate = nowDate.minusYears(1);
                break;
            case Reference.BUDGET_TAB_PAST_2_YEARS:
                fromDate = nowDate.minusYears(2);
                break;
            default:
                fromDate = nowDate.minusWeeks(12);
                break;
        }

        // Refine start of the period to a Sunday
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
                    incomeMap.put(incomeStr, new BudgetTableData(incomeStr, scale));
                }
            } else if (cStr.contains("Expense:")) {
                String expenseStr = EnumUtils.getGeneralCategory(cStr);
                if (!expenseMap.containsKey(expenseStr)) {
                    expenseMap.put(expenseStr, new BudgetTableData(expenseStr, scale));
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
                    } else if (cStr.contains("Expense:")) {
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
        BudgetTableData incomeData = new BudgetTableData("Income", scale);
        BudgetTableData expensData = new BudgetTableData("Expense", scale);

        // Sum up assets
        BudgetTableData assetData = new BudgetTableData("Assets", scale);
        for (Asset asset : Main.assets.getAssetsList()) {
            BudgetTableData data = new BudgetTableData(asset.getName(), scale);
            // Populate data
            for (TransferField tf : asset.getTransferField()) {
                if (DateUtils.isBetweenDatesInclusive(tf.getDate(), fromDate, nowDate)) {
                    data.add(tf.getDate(), tf.getAmount());
                }
            }
            assetItems.getChildren().add(new TreeItem<>(data));
        }

        LocalDate dateIter = fromDate;
        while (dateIter.isBefore(nowDate)) {

            // Asset items
            for (TreeItem<BudgetTableData> item : assetItems.getChildren()) {
                BudgetTableData data = item.getValue();
                double amount = data.getDataDouble(dateIter);
                if (amount != 0.0) {
                    assetData.add(dateIter, amount);
                }
            }
            // Income items
            for (TreeItem<BudgetTableData> item : incomeItems.getChildren()) {
                BudgetTableData data = item.getValue();
                double amount = data.getDataDouble(dateIter);
                if (amount != 0.0) {
                    incomeData.add(dateIter, amount);
                }
            }
            // Expense items
            for (TreeItem<BudgetTableData> item : expenseItems.getChildren()) {
                BudgetTableData data = item.getValue();
                double amount = data.getDataDouble(dateIter);
                if (amount != 0.0) {
                    expensData.add(dateIter, amount);
                }
            }

            // Use correct scale to iterate
            switch (scale) {
                case Reference.BUDGET_TAB_SCALE_WEEKLY:
                    dateIter = dateIter.plusWeeks(1);
                    break;
                case Reference.BUDGET_TAB_SCALE_MONTHLY:
                    dateIter = dateIter.plusMonths(1);
                    break;
                case Reference.BUDGET_TAB_SCALE_YEARLY:
                    dateIter = dateIter.plusYears(1);
                    break;
                default:
                    dateIter = dateIter.plusWeeks(1);
                    break;
            }
        }
        for (LocalDate weekIter = fromDate; weekIter.isBefore(nowDate); weekIter = weekIter.plusWeeks(1)) {

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
        // Reset dateIter
        dateIter = fromDate;
        while (dateIter.isBefore(nowDate)) {
            TreeTableColumn<BudgetTableData, String> newCol;
            LocalDate dateIterCopy = dateIter;
            // Assign and iterate
            switch (scale) {
                case Reference.BUDGET_TAB_SCALE_WEEKLY:
                    newCol = new TreeTableColumn<>(dateIterCopy.plusDays(3).toString());
                    dateIter = dateIter.plusWeeks(1);
                    break;
                case Reference.BUDGET_TAB_SCALE_MONTHLY:
                    String str = dateIterCopy.toString();
                    newCol = new TreeTableColumn<>(str.substring(0, str.length() - 3));
                    dateIter = dateIter.plusMonths(1);
                    break;
                case Reference.BUDGET_TAB_SCALE_YEARLY:
                    newCol = new TreeTableColumn<>(Integer.toString(dateIterCopy.getYear()));
                    dateIter = dateIter.plusYears(1);
                    break;
                default:
                    newCol = new TreeTableColumn<>(dateIterCopy.plusDays(3).toString());
                    dateIter = dateIter.plusWeeks(1);
                    break;
            }
            newCol.setCellValueFactory(
                    (TreeTableColumn.CellDataFeatures<BudgetTableData, String> param) ->
                            new ReadOnlyStringWrapper(param.getValue().getValue().getData(dateIterCopy))
            );
            treeTableView.getColumns().add(newCol);
        }
    }

    private void clearBudgetTable() {
        // Clear every column except the first one
        int columnSize = treeTableView.getColumns().size();
        for (int i = 1; i < columnSize; i++) {
            treeTableView.getColumns().remove(1);
        }
        assetItems.getChildren().clear();
        incomeItems.getChildren().clear();
        expenseItems.getChildren().clear();
        incomeList.clear();
        expenseList.clear();
    }

    @FXML
    public void initialize() {
        ObservableList<String> periodList = FXCollections.observableArrayList(
                Reference.BUDGET_TAB_PAST_12_WEEKS,
                Reference.BUDGET_TAB_PAST_24_WEEKS,
                Reference.BUDGET_TAB_PAST_YEAR,
                Reference.BUDGET_TAB_PAST_2_YEARS);
        periodComboBox.setItems(periodList);
        periodComboBox.setValue(Reference.BUDGET_TAB_PAST_12_WEEKS);
        periodComboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (oldValue.equals(newValue)) {
                    return;
                }
                clearBudgetTable();
                createBudgetTableData(newValue, scaleComboBox.getValue());
            }
        });
        ObservableList<String> scaleList = FXCollections.observableArrayList(
                Reference.BUDGET_TAB_SCALE_WEEKLY,
                Reference.BUDGET_TAB_SCALE_MONTHLY,
                Reference.BUDGET_TAB_SCALE_YEARLY);
        scaleComboBox.setItems(scaleList);
        scaleComboBox.setValue(Reference.BUDGET_TAB_SCALE_WEEKLY);
        scaleComboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue.equals(oldValue)) {
                    return;
                }
                clearBudgetTable();
                createBudgetTableData(periodComboBox.getValue(), newValue);
            }
        });

        root.setExpanded(true);
        assetItems.setExpanded(true);
        incomeItems.setExpanded(true);
        expenseItems.setExpanded(true);

        root.getChildren().add(assetItems);
        root.getChildren().add(incomeItems);
        root.getChildren().add(expenseItems);

        treeTableView.prefHeightProperty().bind(((VBox) treeTableView.getParent()).prefHeightProperty());

        createBudgetTableData(Reference.BUDGET_TAB_PAST_12_WEEKS, Reference.BUDGET_TAB_SCALE_WEEKLY);
    }
}
