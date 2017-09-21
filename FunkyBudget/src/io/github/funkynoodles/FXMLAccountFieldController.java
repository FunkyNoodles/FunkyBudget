package io.github.funkynoodles;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.io.IOException;

public class FXMLAccountFieldController {

    @FXML
    protected void handleEditButton(final ActionEvent event) {
        Button btn = (Button) event.getSource();
        HBox hbox = (HBox) btn.getParent();
        String assetName = ((Label) hbox.getChildren().get(0)).getText();
        TabPane tabPane = (TabPane) hbox.getParent().getParent().getParent();
        SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();
        for (int i = 0; i < tabPane.getTabs().size(); i++) {
            Tab tab = tabPane.getTabs().get(i);
            if (tab.getText() == assetName) {
                // Found the opened tab
                selectionModel.select(tab);
                return;
            }
        }
        // Tab not opened
        Tab newTab = new Tab(assetName);
        GridPane tabContent;
        try {
            tabContent = (GridPane) FXMLLoader.load(getClass().getResource("fxml_account_tab.fxml"));
            newTab.setContent(tabContent);

            int assetIndex = Main.findAssetIndexByName(assetName);

            HBox topHBox = (HBox) tabContent.getChildren().get(0);
            ((Label) topHBox.getChildren().get(0)).setText(assetName);
            ((TextField) topHBox.getChildren().get(1)).setText(Main.assets.getAssetsList().get(assetIndex).getBalanceStr());

            @SuppressWarnings("unchecked")
            TableView<TransferField> tableView = (TableView<TransferField>) tabContent.getChildren().get(1);

            Main.assets.getAssetsList().get(assetIndex).setObservableTransferField(FXCollections.observableArrayList(Main.assets.getAssetsList().get(assetIndex).getTransferField()));
            tableView.setItems(Main.assets.getAssetsList().get(assetIndex).getObservableTransferField());

            tabPane.getTabs().add(newTab);
            selectionModel.select(newTab);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
