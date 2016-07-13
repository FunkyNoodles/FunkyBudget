package io.github.funkynoodles;

import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class FXMLExpenseOverTimeAdvancedController {

	@FXML private VBox vBox;
	@FXML private CheckBox valueCheckBox;
	@FXML private Button newDataSetButton;

	private BorderPane borderPane;

	@FXML public void onValueCheckBoxToggle(){
		borderPane = (BorderPane)vBox.getParent();
		LineChart<String, Number> lineChart = (LineChart) borderPane.getCenter();
		if (valueCheckBox.isSelected()) {
			// TODO show values
			for (Series<String, Number> series : lineChart.getData()) {
				for (XYChart.Data<String, Number> data : series.getData()) {
				}
			}
		}else{
			// TODO not show values
		}
	}

	@FXML public void initialize(){

	}
}
