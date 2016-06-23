package io.github.funkynoodles;

import java.time.LocalDate;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public class FXMLAccountTabController {

	@FXML
    protected void handleAddButton(final ActionEvent event) {
    	Scene scene = ((Button)event.getSource()).getScene();
    	DatePicker datePicker = (DatePicker)scene.lookup("#datePicker");
    	TextField amountTextField = (TextField)scene.lookup("#amountText");
    	String amount = amountTextField.getText();
    	TextField detailsTextField = (TextField)scene.lookup("#detailsText");
    	String details = detailsTextField.getText();
    	int amountNum = 0;
    	LocalDate date = datePicker.getValue();
    	String dateStr = "";
    	try{
    		amountNum = Integer.parseInt(amount);
    		dateStr = date.toString();

    	}catch (NullPointerException e){
    		Alert errorBox = new Alert(AlertType.ERROR);
			errorBox.setTitle("Error");
			errorBox.setHeaderText("Invalid Date");
			errorBox.setContentText("Please Enter a Valid Date");
			errorBox.showAndWait();
			return;
    	}catch (NumberFormatException e){
			Alert errorBox = new Alert(AlertType.ERROR);
			errorBox.setTitle("Error");
			errorBox.setHeaderText("Invalid Number");
			errorBox.setContentText("Please Enter a Valid Number");
			errorBox.showAndWait();
			return;
    	}
    	amountTextField.clear();
    	detailsTextField.clear();
    }
}
