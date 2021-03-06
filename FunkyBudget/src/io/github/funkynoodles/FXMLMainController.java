package io.github.funkynoodles;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class FXMLMainController {

    private int graphOpenedCount = 0;
    private int budgetOpenedCount = 0;

    @FXML
    private TabPane tabPane;

    @FXML
    protected void handleExitAction(final ActionEvent event) {
        Main.exit();
    }

    @FXML
    protected void handleNewAssetButton(final ActionEvent event) {
        Stage newAssetStage = new Stage();
        newAssetStage.setTitle("New Asset");
        Button btn = (Button) event.getSource();
        Scene scene = btn.getScene();
        try {
            GridPane newAssetRoot = (GridPane) FXMLLoader.load(getClass().getResource("fxml_new_asset_window.fxml"));
            newAssetStage.setScene(new Scene(newAssetRoot));
            newAssetStage.initOwner(scene.getWindow());
            newAssetStage.showAndWait();
            newAssetStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void handleReportGraph() {
        Tab newTab = new Tab("Graph " + Integer.toString(++graphOpenedCount));
        Main.assets.sort();
        SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();
        try {
            BorderPane tabContent = (BorderPane) FXMLLoader.load(getClass().getResource("fxml_chart_tab.fxml"));
            newTab.setContent(tabContent);
            tabPane.getTabs().add(newTab);
            selectionModel.select(newTab);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void handleReportBudget() {
        Tab newTab = new Tab("Budget " + Integer.toString(++budgetOpenedCount));
        SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();
        try {
            VBox tabContent = (VBox) FXMLLoader.load(getClass().getResource("fxml_budget_tab.fxml"));
            newTab.setContent(tabContent);
            tabPane.getTabs().add(newTab);
            selectionModel.select(newTab);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void handleOpenFile() {
        if (Main.changed) {
            // Prompt save option
            Alert saveWindow = new Alert(AlertType.WARNING);
            saveWindow.setTitle(Reference.NAME);
            saveWindow.setHeaderText("Do you want to save any changes made?");
            saveWindow.setContentText("Your changes will be lost you don't save them.");

            ButtonType save = new ButtonType("Save");
            ButtonType dontSave = new ButtonType("Don't Save");
            ButtonType cancel = new ButtonType("Cancel");

            saveWindow.getButtonTypes().setAll(save, dontSave, cancel);
            saveWindow.showAndWait().ifPresent(response -> {
                if (response == save) {
                    Main.saveAll();
                } else if (response == dontSave) {
                } else {
                    return;
                }
            });
        }
        Main.loadAll();
    }

    @FXML
    protected void handleSaveFile() {
        Main.saveAll();
    }

    @FXML
    protected void handleSaveAs() {
        String tmpWorkingFileLocation = Main.workingFileLocation;
        Main.workingFileLocation = "";
        if (!Main.saveAll()) {
            Main.workingFileLocation = tmpWorkingFileLocation;
        }
    }

    @FXML
    private void handleKeyInput(final InputEvent event) {
        if (event instanceof KeyEvent) {
            final KeyEvent keyEvent = (KeyEvent) event;
            if (keyEvent.isControlDown() && keyEvent.getCode() == KeyCode.Q) {
                Main.exit();
            }
            if (keyEvent.isControlDown() && keyEvent.isShiftDown() && keyEvent.getCode() == KeyCode.T) {
                Main.readCSVFromGnuCash("C:/Users/Louis/Desktop/test.csv");
            }
        }
    }

    @FXML
    private void handleReadGnuCashransactionCSV() {
        Stage readStage = new Stage();
        readStage.setTitle("Import from GnuCash Transactions CSV");
        Scene scene = tabPane.getScene();
        try {
            VBox readRoot = (VBox) FXMLLoader.load(getClass().getResource("fxml_read_gnucash_trans_csv.fxml"));
            readStage.setScene(new Scene(readRoot));
            readStage.initOwner(scene.getWindow());
            readStage.showAndWait();
            readStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}