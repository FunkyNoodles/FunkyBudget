package io.github.funkynoodles;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class FXMLReadGnuCashTransCSVController {
    @FXML
    TextField filePath;
    @FXML
    Button importButton;
    @FXML
    Button cancelButton;
    @FXML
    Button browseButton;

    @FXML
    private void handleImportButton() {
        String fileName = filePath.getText();
        if (Main.readCSVFromGnuCash(fileName)) {
            Stage stage = (Stage) importButton.getScene().getWindow();
            stage.hide();
        }
    }

    @FXML
    private void handleCancelButton() {
        Stage stage = (Stage) importButton.getScene().getWindow();
        stage.hide();
    }

    @FXML
    private void handleBrowseButton() {
        File file = null;
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Files", "*.*"),
                new FileChooser.ExtensionFilter("CSV", "*.csv")
        );
        file = fileChooser.showOpenDialog(Main.primaryStage);
        if (file != null) {
            filePath.setText(file.getAbsolutePath());
        } else {
            return;
        }
    }
}
