package io.github.funkynoodles;

import java.io.IOException;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class FXMLMainController {
    @FXML
    protected void handleExitAction(final ActionEvent event) {
    	Platform.exit();
    }

    @FXML
    protected void handleNewAssetButton(final ActionEvent event){
    	Stage newAssetStage = new Stage();
    	newAssetStage.setTitle("New Asset");
    	Button btn = (Button) event.getSource();
    	Scene scene = btn.getScene();
    	VBox assetVBox = (VBox)scene.lookup("#assetVBox");
    	try {
			GridPane newAssetRoot = (GridPane)FXMLLoader.load(getClass().getResource("fxml_new_asset_window.fxml"));
			newAssetStage.setScene(new Scene(newAssetRoot));
			newAssetStage.initOwner(scene.getWindow());
			newAssetStage.showAndWait();
			newAssetStage.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	/*Button btn = (Button) event.getSource();
    	Scene scene = btn.getScene();
    	VBox assetVBox = (VBox)scene.lookup("#assetVBox");
    	try {
			assetVBox.getChildren().add(assetVBox.getChildren().size() - 1, (HBox)FXMLLoader.load(getClass().getResource("fxml_account_field.fxml")));
		} catch (IOException e) {
			e.printStackTrace();
		}*/
    }

    @FXML
    private void handleKeyInput(final InputEvent event)
    {
       if (event instanceof KeyEvent)
       {
          final KeyEvent keyEvent = (KeyEvent) event;
          if (keyEvent.isControlDown() && keyEvent.getCode() == KeyCode.A)
          {
          }
       }
    }
}