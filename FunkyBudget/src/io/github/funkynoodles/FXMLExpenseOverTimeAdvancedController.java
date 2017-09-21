package io.github.funkynoodles;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.time.LocalDate;

public class FXMLExpenseOverTimeAdvancedController {

    @FXML
    private VBox vBox;
    @FXML
    private CheckBox valueCheckBox;
    @FXML
    private Button newDataSetButton;

    @FXML
    private ComboBox<String> xAxisScaleComboBox;

    @FXML
    private VBox dataSetVBox;
    @FXML
    private ComboBox<String> categoryComboBox;

    private BorderPane borderPane;

    @FXML
    public void onValueCheckBoxToggle() {
        borderPane = (BorderPane) vBox.getParent();
        @SuppressWarnings("unchecked")
        LineChart<String, Number> lineChart = (LineChart<String, Number>) borderPane.getCenter();

        if (valueCheckBox.isSelected()) {
            for (Series<String, Number> series : lineChart.getData()) {
                for (XYChart.Data<String, Number> data : series.getData()) {
                    ((ChartNode) data.getNode()).show();
                }
            }
        } else {
            for (Series<String, Number> series : lineChart.getData()) {
                for (XYChart.Data<String, Number> data : series.getData()) {
                    ((ChartNode) data.getNode()).hide();
                }
            }
        }
    }

    @FXML
    public void handleNewDataSetButton() {
        borderPane = (BorderPane) vBox.getParent();
        String category = categoryComboBox.getValue();
        if (category == null) {
            Alert errorBox = new Alert(AlertType.ERROR);
            errorBox.setTitle(Reference.NAME);
            errorBox.setHeaderText("Invalid Category");
            errorBox.setContentText("Please choose a category");
            errorBox.showAndWait();
            return;
        }
        // Obtain chart info
        String assetName = ((Label) borderPane.lookup("#infoAssetSelectedLabel")).getText();
        int assetIndex = Main.findAssetIndexByName(assetName);
        Asset asset = Main.assets.getAssetsList().get(assetIndex);
        LocalDate fromDate = LocalDate.parse(((Label) borderPane.lookup("#infoFromDateLabel")).getText());
        LocalDate toDate = LocalDate.parse(((Label) borderPane.lookup("#infoToDateLabel")).getText());
        String xScale = ((Label) borderPane.lookup("#infoXScaleLabel")).getText();
        @SuppressWarnings("unchecked")
        LineChart<String, Number> lineChart = (LineChart<String, Number>) borderPane.getCenter();
        // Add series
        XYChart.Series<String, Number> series = ChartGenerator.generateExpenseOverTimeSeries(asset, fromDate, toDate, category, lineChart.getData().size(), xScale);
        if (series.getData().size() == 0) {
            // No info to display
            Alert errorBox = new Alert(AlertType.INFORMATION);
            errorBox.setTitle(Reference.NAME);
            errorBox.setHeaderText("No Data Points");
            errorBox.setContentText("This category does not seem to contain data");
            errorBox.showAndWait();
            return;
        }
        series.setName(category);
        lineChart.getData().add(series);
        // Add category field
        int indexToAdd = dataSetVBox.getChildren().size() - 1;
        HBox categoryField = null;
        try {
            categoryField = FXMLLoader.load(getClass().getResource("fxml_expense_over_time_category_field.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        ((Label) categoryField.getChildren().get(0)).setText(category);
        dataSetVBox.getChildren().add(indexToAdd, categoryField);
    }

    @FXML
    public void initialize() {
        xAxisScaleComboBox.setItems(FXCollections.observableArrayList(
                Reference.CHART_X_AXIS_SCALE_DAILY,
                Reference.CHART_X_AXIS_SCALE_WEEKLY,
                Reference.CHART_X_AXIS_SCALE_MONTHLY,
                Reference.CHART_X_AXIS_SCALE_YEARLY));
        xAxisScaleComboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                borderPane = (BorderPane) vBox.getParent();
                String currentXScale = ((Label) borderPane.lookup("#infoXScaleLabel")).getText();
                if (!currentXScale.equals(newValue)) {
                    ChartGenerator.generateExpenseOverTimeChart(borderPane, false, newValue);
                }
            }
        });
        EnumUtils.populateCategoryComboBox(categoryComboBox, null, 1);
    }
}
