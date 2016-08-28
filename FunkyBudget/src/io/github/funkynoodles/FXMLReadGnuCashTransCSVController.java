package io.github.funkynoodles;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class FXMLReadGnuCashTransCSVController {
	@FXML TextField filePath;
	@FXML Button importButton;
	@FXML Button cancelButton;

	@FXML
	private void handleImportButton(){
		String fileName = filePath.getText();
		if (!Main.readCSVfromGnuCash(fileName)) {

		}
		Stage stage = (Stage)importButton.getScene().getWindow();
		stage.hide();
	}

	@FXML
	private void handleCancelButton() {
		Stage stage = (Stage)importButton.getScene().getWindow();
		stage.hide();
	}
}
