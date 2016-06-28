package io.github.funkynoodles;

import java.io.IOException;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

public class FXMLAccountFieldController {

	@SuppressWarnings("unchecked")
	@FXML
	protected void handleEditButton(final ActionEvent event){
		Button btn = (Button)event.getSource();
		HBox hbox = (HBox)btn.getParent();
		String assetName = ((Label)hbox.getChildren().get(0)).getText();
		TabPane tabPane = (TabPane)hbox.getParent().getParent().getParent();
		SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();
		for (int i = 0; i < tabPane.getTabs().size(); i++) {
			Tab tab = tabPane.getTabs().get(i);
			if(tab.getText() == assetName){
				// Found the opened tab
				selectionModel.select(tab);
				return;
			}
		}
		// Tab not opened
		Tab newTab = new Tab(assetName);
		GridPane tabContent;
		try {
			tabContent = (GridPane)FXMLLoader.load(getClass().getResource("fxml_account_tab.fxml"));
			newTab.setContent(tabContent);

			int assetIndex = Main.findAssetIndexByName(assetName);

			HBox topHBox = (HBox)tabContent.getChildren().get(0);
			((Label)topHBox.getChildren().get(0)).setText(assetName);
			((TextField)topHBox.getChildren().get(1)).setText(Main.assets.getAssetsList().get(assetIndex).getBalanceStr());


			// Populate choice box
			ComboBox<String> comboBox = (ComboBox<String>)((HBox)tabContent.getChildren().get(2)).getChildren().get(3);
			CategoryUtils.populateCategoryChoiceBox(comboBox, Main.assets.getAssetsList().get(assetIndex));

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
			balanceCol.setCellValueFactory(new PropertyValueFactory<TransferField, String>("balanceStr"));


			Main.assets.getAssetsList().get(assetIndex).setObservableTransferField(FXCollections.observableArrayList(Main.assets.getAssetsList().get(assetIndex).getTransferField()));
			tableView.setItems(Main.assets.getAssetsList().get(assetIndex).getObservableTransferField());

			tabPane.getTabs().add(newTab);
			selectionModel.select(newTab);
		} catch (IOException e) {
			e.printStackTrace();
		}


	}
}
