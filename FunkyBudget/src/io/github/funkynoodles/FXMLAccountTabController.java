package io.github.funkynoodles;

import java.time.LocalDate;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

public class FXMLAccountTabController {

	@FXML
    protected void handleAddButton(final ActionEvent event) {
		Button btn = (Button)event.getSource();
		Scene scene = btn.getScene();
    	HBox hbox = (HBox)btn.getParent();
    	DatePicker datePicker = (DatePicker)hbox.getChildren().get(0);
    	TextField amountTextField = (TextField)hbox.getChildren().get(1);
    	String amount = amountTextField.getText();
    	TextField detailsTextField = (TextField)hbox.getChildren().get(2);
    	String details = detailsTextField.getText();
    	ChoiceBox<String> choiceBox = (ChoiceBox<String>)hbox.getChildren().get(3);
    	double amountNum = 0;
    	LocalDate date = datePicker.getValue();
    	String dateStr = "";
    	String c = choiceBox.getValue();

    	try{
    		amountNum = Double.parseDouble(amount);
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
    	if(c == null){
    		Alert errorBox = new Alert(AlertType.ERROR);
			errorBox.setTitle("Error");
			errorBox.setHeaderText("Invalid Category");
			errorBox.setContentText("Please Select a Category");
			errorBox.showAndWait();
			return;
    	}
    	amountTextField.clear();
    	detailsTextField.clear();

    	TransferField tf = new TransferField(date, amountNum, details, CategoryUtils.toCategory(c));
    	Asset asset;
    	TabPane tabPane = (TabPane) scene.lookup("#tabPane");
    	Tab tab = tabPane.getSelectionModel().getSelectedItem();
    	String tabName = tab.getText();

    	int assetIndex = Main.findAssetIndexByName(tabName);
    	asset = Main.assets.getAssetsList().get(assetIndex);
    	asset.insert(tf);

    	GridPane tabContent = (GridPane)tab.getContent();
    	TableView<TransferField> tableView = (TableView<TransferField>)tabContent.getChildren().get(1);
    	TableColumn<TransferField, String> dateCol = (TableColumn<TransferField, String>)tableView.getColumns().get(0);
    	TableColumn<TransferField, String> detailCol = (TableColumn<TransferField, String>)tableView.getColumns().get(1);
		TableColumn<TransferField, String> categoryCol = (TableColumn<TransferField, String>)tableView.getColumns().get(2);
		TableColumn<TransferField, String> depositCol = (TableColumn<TransferField, String>)tableView.getColumns().get(3);
		TableColumn<TransferField, String> withdrawlCol = (TableColumn<TransferField, String>)tableView.getColumns().get(4);
		TableColumn<TransferField, String> balanceCol = (TableColumn<TransferField, String>)tableView.getColumns().get(5);

		dateCol.setCellValueFactory(new PropertyValueFactory<TransferField, String>("dateStr"));
		detailCol.setCellValueFactory(new PropertyValueFactory<TransferField, String>("detailStr"));
		categoryCol.setCellValueFactory(new PropertyValueFactory<TransferField, String>("categoryStr"));
		depositCol.setCellValueFactory(new PropertyValueFactory<TransferField, String>("depositStr"));
		withdrawlCol.setCellValueFactory(new PropertyValueFactory<TransferField, String>("withdrawlStr"));

    }

	@FXML
	protected void handleKeyInput(final InputEvent event){
		if (event instanceof KeyEvent)
		{
			final KeyEvent keyEvent = (KeyEvent) event;
			if (keyEvent.isControlDown() && keyEvent.getCode() == KeyCode.ENTER){
			}
		}
	}
}
