package io.github.funkynoodles;

import java.io.File;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class FXMLMainController {
    @FXML
    protected void handleExitAction(final ActionEvent event) {
    	Main.exit();
    }

    @FXML
    protected void handleNewAssetButton(final ActionEvent event){
    	Stage newAssetStage = new Stage();
    	newAssetStage.setTitle("New Asset");
    	Button btn = (Button) event.getSource();
    	Scene scene = btn.getScene();
    	try {
			GridPane newAssetRoot = (GridPane)FXMLLoader.load(getClass().getResource("fxml_new_asset_window.fxml"));
			newAssetStage.setScene(new Scene(newAssetRoot));
			newAssetStage.initOwner(scene.getWindow());
			newAssetStage.showAndWait();
			newAssetStage.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    @FXML
    private void handleKeyInput(final InputEvent event)
    {
       if (event instanceof KeyEvent)
       {
          final KeyEvent keyEvent = (KeyEvent) event;
          if (keyEvent.isControlDown() && keyEvent.getCode() == KeyCode.Q)
          {
        	  Main.exit();
          }
          if (keyEvent.isControlDown() && keyEvent.getCode() == KeyCode.S)
          {
        	  Main.saveAll();
          }
          if(keyEvent.isControlDown() && keyEvent.getCode() == KeyCode.O){
        	  Main.loadAll();
          }
       }
    }
}