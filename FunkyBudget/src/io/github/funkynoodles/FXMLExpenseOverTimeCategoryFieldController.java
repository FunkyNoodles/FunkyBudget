package io.github.funkynoodles;

import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class FXMLExpenseOverTimeCategoryFieldController {

	@FXML private Label categoryLabel;
	@FXML private CheckBox displayCheckBox;
	@FXML private Button removeButton;

	@FXML private HBox hBox;

	private BorderPane borderPane;
	int seriesIndex = 1;
	XYChart.Series<String, Number> series;

	@FXML
	public void handleDisplayCheckBoxToggle(){
		borderPane = (BorderPane)hBox.getParent().getParent().getParent();
		@SuppressWarnings("unchecked")
		LineChart<String, Number> lineChart = (LineChart<String, Number>) borderPane.getCenter();
		if (displayCheckBox.isSelected()) {
			lineChart.getData().add(seriesIndex, series);
		}else{
			seriesIndex = findSeriesByName(lineChart, categoryLabel.getText());
			// Save the series
			series = lineChart.getData().get(seriesIndex);
			lineChart.getData().remove(seriesIndex);
		}
	}

	@FXML
	public void handleRemoveButton(){
		borderPane = (BorderPane)hBox.getParent().getParent().getParent();
		if (displayCheckBox.isSelected()) {
			@SuppressWarnings("unchecked")
			LineChart<String, Number> lineChart = (LineChart<String, Number>) borderPane.getCenter();
			seriesIndex = findSeriesByName(lineChart, categoryLabel.getText());
			lineChart.getData().remove(seriesIndex);
		}
		int fieldIndex = seriesIndex - 1;
		VBox dataSetVBox = (VBox) hBox.getParent();
		dataSetVBox.getChildren().remove(fieldIndex);

	}

	private int findSeriesByName(LineChart<String, Number> lineChart, String name){
		XYChart.Series<String, Number> series;
		for (int i = 0; i < lineChart.getData().size(); ++i) {
			series = lineChart.getData().get(i);
			if (series.getName().equals(name)) {
				return i;
			}
		}
		return -1;
	}

	@FXML
	public void initialize(){
		displayCheckBox.setSelected(true);
	}
}
