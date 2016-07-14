package io.github.funkynoodles;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class FXMLExpenseOverTimeAdvancedController {

	@FXML private VBox vBox;
	@FXML private CheckBox valueCheckBox;
	@FXML private Button newDataSetButton;

	@FXML private ComboBox<String> xAxisScaleComboBox;

	private BorderPane borderPane;

	@FXML public void onValueCheckBoxToggle(){

		borderPane = (BorderPane)vBox.getParent();
		@SuppressWarnings("unchecked")
		LineChart<String, Number> lineChart = (LineChart<String, Number>) borderPane.getCenter();

		if (valueCheckBox.isSelected()) {
			for (Series<String, Number> series : lineChart.getData()) {
				for (XYChart.Data<String, Number> data : series.getData()) {
					((ChartNode)data.getNode()).show();
				}
			}
		}else{
			for (Series<String, Number> series : lineChart.getData()) {
				for (XYChart.Data<String, Number> data : series.getData()) {
					((ChartNode)data.getNode()).hide();
				}
			}
		}
	}

	@FXML public void initialize(){
		xAxisScaleComboBox.setItems(FXCollections.observableArrayList(
				Reference.CHART_X_AXIS_SCALE_DAILY,
				Reference.CHART_X_AXIS_SCALE_WEEKLY,
				Reference.CHART_X_AXIS_SCALE_MONTHLY,
				Reference.CHART_X_AXIS_SCALE_YEARLY));
		xAxisScaleComboBox.valueProperty().addListener(new ChangeListener<String>(){
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				borderPane = (BorderPane)vBox.getParent();
				String currentXScale = ((Label)borderPane.lookup("#infoXScaleLabel")).getText();
				if (!currentXScale.equals(newValue)) {
					ChartGenerator.generateExpenseOverTimeChart(borderPane, false, newValue);
				}
			}
		});
	}
}
