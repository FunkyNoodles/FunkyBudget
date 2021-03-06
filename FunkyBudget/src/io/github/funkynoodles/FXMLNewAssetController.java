package io.github.funkynoodles;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class FXMLNewAssetController {

    @FXML
    private ComboBox<String> newAssetTypeComboBox;

    @SuppressWarnings("unchecked")
    @FXML
    protected void handleCreateButton(final ActionEvent event) {
        Button btn = (Button) event.getSource();
        Scene scene = btn.getScene();

        Stage stage = (Stage) scene.getWindow();
        Scene rootScene = ((Stage) btn.getScene().getWindow()).getOwner().getScene();
        VBox assetVBox = (VBox) rootScene.lookup("#assetVBox");
        TabPane tabPane = (TabPane) rootScene.lookup("#tabPane");
        Tab newTab = new Tab();
        String assetName = ((TextField) scene.lookup("#newAssetName")).getText();
        String assetTypeString = newAssetTypeComboBox.getValue();

        if (assetName.isEmpty()) {
            Alert errorBox = new Alert(AlertType.ERROR);
            errorBox.setTitle(Reference.NAME);
            errorBox.setHeaderText("Invalid Name");
            errorBox.setContentText("Please enter valid information");
            errorBox.showAndWait();
            return;
        }
        if (Main.isNameTaken(assetName)) {
            Alert errorBox = new Alert(AlertType.ERROR);
            errorBox.setTitle(Reference.NAME);
            errorBox.setHeaderText("Asset Name Taken");
            errorBox.setContentText("Please enter another asset name");
            errorBox.showAndWait();
            return;
        }
        if (assetTypeString == null) {
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
            HBox assetField = (HBox) FXMLLoader.load(getClass().getResource("fxml_account_field.fxml"));
            ((Label) assetField.getChildren().get(0)).setText(assetName);
            ((TextField) assetField.getChildren().get(1)).setText("0.00");
            assetVBox.getChildren().add(assetVBox.getChildren().size() - 1, assetField);
            newTab.setText(assetName);

            GridPane tabContent = (GridPane) FXMLLoader.load(getClass().getResource("fxml_account_tab.fxml"));
            HBox topHBox = (HBox) tabContent.getChildren().get(0);
            ((Label) topHBox.getChildren().get(0)).setText(assetName);
            newTab.setContent(tabContent);
            newTab.setId("tab" + assetName);

            Main.assets.insert(newAsset);

            TableView<TransferField> tableView = (TableView<TransferField>) tabContent.getChildren().get(1);

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
    protected void handleCancelButton(final ActionEvent event) {
        Button btn = (Button) event.getSource();
        Stage stage = (Stage) btn.getScene().getWindow();
        stage.hide();
    }

    @FXML
    protected void initialize() {
        EnumUtils.populateNewAssetTypeComboBox(newAssetTypeComboBox);
    }
}