package io.github.funkynoodles;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class FXMLNewAssetController {

    @FXML
    protected void handleCreateButton(final ActionEvent event) {
    	Button btn = (Button) event.getSource();
    	Stage stage = (Stage)btn.getScene().getWindow();
    	VBox assetVBox = (VBox) ((Stage)btn.getScene().getWindow()).getOwner().getScene().lookup("#assetVBox");
    	try {
			assetVBox.getChildren().add(assetVBox.getChildren().size() - 1, (HBox)FXMLLoader.load(getClass().getResource("fxml_account_field.fxml")));
			stage.hide();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    @FXML
    protected void handleCancelButton(final ActionEvent event){
    	Button btn = (Button) event.getSource();
    	Stage stage = (Stage) btn.getScene().getWindow();
    	stage.hide();
    }
}