package io.github.funkynoodles;

import java.time.LocalDate;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;

public class FXMLChartTabController {

	@FXML private ComboBox<String> assetComboBox;
	@FXML private ComboBox<String> periodComboBox;
	@FXML private Label fromLabel;
	@FXML private DatePicker fromDatePicker;
	@FXML private Label toLabel;
	@FXML private DatePicker toDatePicker;
	@FXML private Label invalidDateLabel;

	@FXML
	public void initialize(){
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
				}else{
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
				try{
					toDate.toString();
				}catch(NullPointerException e){
					return;
				}
				if (toDate.isBefore(newValue)) {
					invalidDateLabel.setVisible(true);
				}else {
					invalidDateLabel.setVisible(false);
				}
			}
		});
		toDatePicker.valueProperty().addListener(new ChangeListener<LocalDate>() {
			@Override
			public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
				LocalDate fromDate = fromDatePicker.getValue();
				try{
					fromDate.toString();
				}catch(NullPointerException e){
					return;
				}
				if (fromDate.isAfter(newValue)) {
					invalidDateLabel.setVisible(true);
				}else {
					invalidDateLabel.setVisible(false);
				}
			}
		});
	}
}
