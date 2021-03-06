package io.github.funkynoodles;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.time.LocalDate;

public class FXMLChartTabController {

    @FXML
    private BorderPane borderPane;

    // Top pane
    @FXML
    private ComboBox<String> assetComboBox;
    @FXML
    private ComboBox<String> periodComboBox;
    @FXML
    private Label fromLabel;
    @FXML
    private DatePicker fromDatePicker;
    @FXML
    private Label toLabel;
    @FXML
    private DatePicker toDatePicker;
    @FXML
    private Label invalidDateLabel;

    // Left pane
    @FXML
    private ComboBox<String> chartTypeComboBox;
    @FXML
    private Button generateButton;
    @FXML
    private Label infoAssetSelectedLabel;
    @FXML
    private Label infoFromDateLabel;
    @FXML
    private Label infoToDateLabel;

    @FXML
    public void handleGenerateButton() {
        String periodStr = periodComboBox.getValue();
        String chartTypeStr = chartTypeComboBox.getValue();
        String assetStr = assetComboBox.getValue();
        if (assetStr == null) {
            Alert errorBox = new Alert(AlertType.ERROR);
            errorBox.setTitle(Reference.NAME);
            errorBox.setHeaderText("Invalid Asset");
            errorBox.setContentText("Please choose an asset");
            errorBox.showAndWait();
            return;
        }
        if (periodStr == null) {
            Alert errorBox = new Alert(AlertType.ERROR);
            errorBox.setTitle(Reference.NAME);
            errorBox.setHeaderText("Invalid Time Period");
            errorBox.setContentText("Please choose a time period");
            errorBox.showAndWait();
            return;
        }
        if (chartTypeStr == null) {
            Alert errorBox = new Alert(AlertType.ERROR);
            errorBox.setTitle(Reference.NAME);
            errorBox.setHeaderText("Invalid Chart Type");
            errorBox.setContentText("Please choose a chart type");
            errorBox.showAndWait();
            return;
        }

        int assetIndex = Main.findAssetIndexByName(assetStr);
        Asset asset = Main.assets.getAssetsList().get(assetIndex);

        LocalDate fromDate = null, toDate = LocalDate.now();
        ;
        switch (periodStr) {
            // TODO select time period
            case Reference.PERIOD_COMBOBOX_PAST_WEEK:
                fromDate = toDate.minusWeeks(1);
                break;
            case Reference.PERIOD_COMBOBOX_PAST_2_WEEK:
                fromDate = toDate.minusWeeks(2);
                break;
            case Reference.PERIOD_COMBOBOX_PAST_MONTH:
                fromDate = toDate.minusMonths(1);
                break;
            case Reference.PERIOD_COMBOBOX_PAST_YEAR:
                fromDate = toDate.minusYears(1);
                break;
            case Reference.PERIOD_COMBOBOC_CUSTOM:
                fromDate = fromDatePicker.getValue();
                toDate = toDatePicker.getValue();
                if (fromDate.isAfter(toDate)) {
                    Alert errorBox = new Alert(AlertType.ERROR);
                    errorBox.setTitle(Reference.NAME);
                    errorBox.setHeaderText("Invalid Date Order");
                    errorBox.setContentText("Please choose proper dates");
                    errorBox.showAndWait();
                    return;
                }
                break;
            default:
                return;
        }

        // Set Chart Info Table
        infoAssetSelectedLabel.setText(assetStr);
        infoFromDateLabel.setText(fromDate.toString());
        infoToDateLabel.setText(toDate.toString());
        switch (chartTypeStr) {
            // TODO complete other types
            case Reference.CHART_TYPE_EXPENSE_PIE_CHART:
                ChartGenerator.generateExpensePieChart(borderPane, asset, fromDate, toDate);
                try {
                    VBox pieChartAdvanced = (VBox) FXMLLoader.load(getClass().getResource("fxml_expense_pie_chart_advanced.fxml"));
                    borderPane.setRight(pieChartAdvanced);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case Reference.CHART_TYPE_EXPENSE_OVER_TIME:
                ChartGenerator.generateExpenseOverTimeChart(borderPane, asset, fromDate, toDate, true, null);
                try {
                    VBox lineChartAdvanced = (VBox) FXMLLoader.load(getClass().getResource("fxml_expense_over_time_advanced.fxml"));
                    borderPane.setRight(lineChartAdvanced);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }

    @FXML
    public void initialize() {
        //borderPane.getTop().getStyleClass().add("border-pane");
        assetComboBox.setItems(Main.assets.getAssetObservableList());
        ObservableList<String> periodComboBoxList = FXCollections.observableArrayList(
                Reference.PERIOD_COMBOBOX_PAST_WEEK,
                Reference.PERIOD_COMBOBOX_PAST_2_WEEK,
                Reference.PERIOD_COMBOBOX_PAST_MONTH,
                Reference.PERIOD_COMBOBOX_PAST_YEAR,
                Reference.PERIOD_COMBOBOC_CUSTOM
        );
        periodComboBox.setItems(periodComboBoxList);
        periodComboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue == Reference.PERIOD_COMBOBOC_CUSTOM) {
                    fromLabel.setVisible(true);
                    fromDatePicker.setVisible(true);
                    toLabel.setVisible(true);
                    toDatePicker.setVisible(true);
                    fromDatePicker.setValue(LocalDate.now());
                    toDatePicker.setValue(LocalDate.now());
                } else {
                    fromLabel.setVisible(false);
                    fromDatePicker.setVisible(false);
                    toLabel.setVisible(false);
                    toDatePicker.setVisible(false);
                }
            }
        });
        fromDatePicker.valueProperty().addListener(new ChangeListener<LocalDate>() {
            @Override
            public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
                LocalDate toDate = toDatePicker.getValue();
                try {
                    toDate.toString();
                } catch (NullPointerException e) {
                    return;
                }
                if (toDate.isBefore(newValue)) {
                    invalidDateLabel.setVisible(true);
                } else {
                    invalidDateLabel.setVisible(false);
                }
            }
        });
        toDatePicker.valueProperty().addListener(new ChangeListener<LocalDate>() {
            @Override
            public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
                LocalDate fromDate = fromDatePicker.getValue();
                try {
                    fromDate.toString();
                } catch (NullPointerException e) {
                    return;
                }
                if (fromDate.isAfter(newValue)) {
                    invalidDateLabel.setVisible(true);
                } else {
                    invalidDateLabel.setVisible(false);
                }
            }
        });
        ObservableList<String> chartTypeComboBoxList = FXCollections.observableArrayList(
                // TODO complete chart types
                Reference.CHART_TYPE_EXPENSE_PIE_CHART,
                Reference.CHART_TYPE_EXPENSE_OVER_TIME
        );
        chartTypeComboBox.setItems(chartTypeComboBoxList);
        chartTypeComboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue.equals(Reference.CHART_TYPE_EXPENSE_PIE_CHART)) {
                    // TODO do pie chart advanced settings
                }
            }
        });
    }
}
