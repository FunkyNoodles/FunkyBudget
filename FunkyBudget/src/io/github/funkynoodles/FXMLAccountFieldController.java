package io.github.funkynoodles;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

public class FXMLAccountFieldController {

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
			// TODO load save files
			newTab.setContent(tabContent);
			newTab.setId("tab" + assetName);
			tabPane.getTabs().add(newTab);
			selectionModel.select(newTab);
		} catch (IOException e) {
			e.printStackTrace();
		}


	}
}
