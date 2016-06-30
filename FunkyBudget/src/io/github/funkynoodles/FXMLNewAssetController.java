package io.github.funkynoodles;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class FXMLNewAssetController {

	@FXML private ComboBox<String> newAssetTypeComboBox;

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
    	String assetTypeString = newAssetTypeComboBox.getValue();

    	if (assetName.isEmpty()){
			Alert errorBox = new Alert(AlertType.ERROR);
			errorBox.setTitle(Reference.NAME);
			errorBox.setHeaderText("Invalid Name");
			errorBox.setContentText("Please enter valid information");
			errorBox.showAndWait();
			return;
		}
    	if (Main.isNameTaken(assetName)){
			Alert errorBox = new Alert(AlertType.ERROR);
			errorBox.setTitle(Reference.NAME);
			errorBox.setHeaderText("Asset Name Taken");
			errorBox.setContentText("Please enter another asset name");
			errorBox.showAndWait();
			return;
		}
    	if (assetTypeString == null){
    		Alert errorBox = new Alert(AlertType.ERROR);
			errorBox.setTitle(Reference.NAME);
			errorBox.setHeaderText("Invalid Asset Type");
			errorBox.setContentText("Please choose an asset type");
			errorBox.showAndWait();
			return;
    	}
    	Asset newAsset = new Asset(assetName, EnumUtils.assetTypeMap.inverse().get(assetTypeString));
    	SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();

    	try {
    		HBox assetField = (HBox)FXMLLoader.load(getClass().getResource("fxml_account_field.fxml"));
    		((Label)assetField.getChildren().get(0)).setText(assetName);
    		((TextField)assetField.getChildren().get(1)).setText("0.00");
			assetVBox.getChildren().add(assetVBox.getChildren().size() - 1, assetField);
			newTab.setText(assetName);

			GridPane tabContent = (GridPane)FXMLLoader.load(getClass().getResource("fxml_account_tab.fxml"));
			HBox topHBox = (HBox)tabContent.getChildren().get(0);
			((Label)topHBox.getChildren().get(0)).setText(assetName);
			newTab.setContent(tabContent);
			newTab.setId("tab" + assetName);

			// Populate choice box
			ComboBox<String> comboBox = (ComboBox<String>)((HBox)tabContent.getChildren().get(2)).getChildren().get(3);
			EnumUtils.populateCategoryComboBox(comboBox, newAsset);

			Main.assets.insert(newAsset);

			TableView<TransferField> tableView = (TableView<TransferField>)tabContent.getChildren().get(1);
			TableColumn<TransferField, String> dateCol = (TableColumn<TransferField, String>)tableView.getColumns().get(0);
			TableColumn detailCol = (TableColumn)tableView.getColumns().get(1);
			TableColumn categoryCol = (TableColumn)tableView.getColumns().get(2);
			TableColumn depositCol = (TableColumn)tableView.getColumns().get(3);
			TableColumn withdrawlCol = (TableColumn)tableView.getColumns().get(4);
			TableColumn balanceCol = (TableColumn)tableView.getColumns().get(5);

			int assetIndex = Main.findAssetIndexByName(assetName);

			tableView.setItems(Main.assets.getAssetsList().get(assetIndex).getObservableTransferField());

			tabPane.getTabs().add(newTab);
			selectionModel.select(newTab);

			Main.changed = true;

			stage.hide();
		} catch (IOException e) {
		}
    }

    @FXML
    protected void handleCancelButton(final ActionEvent event){
    	Button btn = (Button) event.getSource();
    	Stage stage = (Stage) btn.getScene().getWindow();
    	stage.hide();
    }

    @FXML
    protected void initialize(){
    	EnumUtils.populateAssetTypeMap();
    	EnumUtils.populateNewAssetTypeComboBox(newAssetTypeComboBox);
    }
}