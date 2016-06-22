package io.github.funkynoodles;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class FXMLNewAssetController {

    @FXML
    protected void handleCreateButton(final ActionEvent event) {
    	Button btn = (Button) event.getSource();
    }

    @FXML
    protected void handleCancelButton(final ActionEvent event){
    	Button btn = (Button) event.getSource();
    	Stage stage = (Stage) btn.getScene().getWindow();
    	stage.hide();
    }
}