package io.github.funkynoodles;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class FXMLNewAssetController {

    @FXML
    protected void handleCreateButton(final ActionEvent event) {
    	Button btn = (Button) event.getSource();
    	Scene scene = btn.getScene();

    	Stage stage = (Stage)scene.getWindow();
    	Scene rootScene = ((Stage)btn.getScene().getWindow()).getOwner().getScene();
    	VBox assetVBox = (VBox) rootScene.lookup("#assetVBox");
    	TabPane tabPane = (TabPane)rootScene.lookup("#tabPane");
    	Tab newTab = new Tab();
    	String assetName = ((TextField)scene.lookup("#newAssetName")).getText();

    	//String assetType = ((ChoiceBox)scene.lookup("#newAssetType")).getValue().toString();
    	if (assetName.isEmpty() ){//|| assetType.isEmpty()) {
			Alert errorBox = new Alert(AlertType.ERROR);
			errorBox.setTitle("Error");
			errorBox.setHeaderText("Invalid Information");
			errorBox.setContentText("Please Enter Valid Information");
			errorBox.showAndWait();
		}else{
	    	Asset newAccount = new Asset(assetName, AssetType.BANK_CHECKINGS);
	    	try {
				assetVBox.getChildren().add(assetVBox.getChildren().size() - 1, (HBox)FXMLLoader.load(getClass().getResource("fxml_account_field.fxml")));
				newTab.setText(assetName);

				GridPane tabContent = (GridPane)FXMLLoader.load(getClass().getResource("fxml_account_tab.fxml"));
				newTab.setContent(tabContent);
				newTab.setId("tab" + assetName);
				TableView<TransferField> tableView = (TableView<TransferField>)tabContent.getChildren().get(1);
				TableColumn<TransferField, DatePicker> dateCol = (TableColumn<TransferField, DatePicker>)tableView.getColumns().get(0);
				TableColumn detailCol = (TableColumn)tableView.getColumns().get(1);
				TableColumn categoryCol = (TableColumn)tableView.getColumns().get(2);
				TableColumn depositCol = (TableColumn)tableView.getColumns().get(3);
				TableColumn withdrawlCol = (TableColumn)tableView.getColumns().get(4);
				TableColumn balanceCol = (TableColumn)tableView.getColumns().get(5);
				dateCol.setCellValueFactory(new PropertyValueFactory<TransferField, DatePicker>(assetName));
				tabPane.getTabs().add(newTab);
				stage.hide();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
    }

    @FXML
    protected void handleCancelButton(final ActionEvent event){
    	Button btn = (Button) event.getSource();
    	Stage stage = (Stage) btn.getScene().getWindow();
    	stage.hide();
    }
}