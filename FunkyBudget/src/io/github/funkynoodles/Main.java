package io.github.funkynoodles;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Main extends Application{

	public static Assets assets = new Assets();

	public static boolean changed = false;

	public static String workingFileLocation = "";

	public static Stage primaryStage;

	public static int findAssetIndexByName(String name){
		for (int i = 0; i < assets.size(); i++) {
			if (assets.getAssetsList().get(i).getName() == name) {
				return i;
			}
		}
		return -1;
	}
	// Return true if the file is successfully loaded
	@SuppressWarnings("unused")
	public static boolean loadAll(){
		File file = null;
		// Choose a file to save
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open");
		fileChooser.getExtensionFilters().addAll(
			new FileChooser.ExtensionFilter("All Files", "*.*"),
			new FileChooser.ExtensionFilter("JSON", "*.json")
		);
		file = fileChooser.showOpenDialog(primaryStage);
		if (file != null) {
			workingFileLocation = file.getAbsolutePath();
		}else{
			return false;
		}
		Gson gson = new Gson();
		try {
			SaveAssets saveAssets = new SaveAssets();
			Reader reader = new FileReader(file);
			saveAssets = gson.fromJson(reader, SaveAssets.class);
			Main.assets.getAssetsList().clear();
			Main.assets.loadFromSave(saveAssets);
			try {
				reader.close();
			} catch (IOException e) {
				return false;
			}
		} catch (Exception e) {
			Alert erroBox = new Alert(AlertType.ERROR);
			erroBox.setTitle(Reference.NAME);
			erroBox.setHeaderText("Error opening file");
			erroBox.setContentText("Please try again");
			erroBox.showAndWait();
			e.printStackTrace();
			return false;
		}

		TabPane tabPane = (TabPane) primaryStage.getScene().lookup("#tabPane");

		Tab rootTab = null;
		for (int i = 0; i < tabPane.getTabs().size(); ++i) {
			rootTab = tabPane.getTabs().get(i);
			if (rootTab.getId().equals("rootTabAsset")) {
				break;
			}
			return false;
		}
		VBox rootTabVBox = (VBox)rootTab.getContent();
		for (int i = 0; i < assets.size(); i++) {
			String assetName = assets.getAssetsList().get(i).getName();
			String assetBalance = assets.getAssetsList().get(i).getBalanceStr();
			try {
				HBox assetField = (HBox)FXMLLoader.load(Main.class.getResource("fxml_account_field.fxml"));
				((Label)assetField.getChildren().get(0)).setText(assetName);
	    		((TextField)assetField.getChildren().get(1)).setText(assetBalance);
	    		rootTabVBox.getChildren().add(rootTabVBox.getChildren().size() - 1, assetField);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return true;
	}

	public static boolean saveAll(){
		SaveAssets assetsSave = new SaveAssets();
		assetsSave.setVersion(Reference.VERSION);
		for(int i = 0; i < assets.size(); i ++){
			assetsSave.insert(new SaveAsset(assets.getAssetsList().get(i)));
		}
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		if (workingFileLocation.equals("")) {
			// Choose a file to save
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Save As");
			fileChooser.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("All Files", "*.*"),
				new FileChooser.ExtensionFilter("JSON", "*.json")
			);
			File file = fileChooser.showSaveDialog(null);
			if (file != null) {
				workingFileLocation = file.getAbsolutePath();
			}else{
				return false;
			}
		}

		try {
			Writer writer = new FileWriter(workingFileLocation);
			gson.toJson(assetsSave, writer);
			writer.close();
		} catch (IOException e) {
			Alert erroBox = new Alert(AlertType.ERROR);
			erroBox.setTitle(Reference.NAME);
			erroBox.setHeaderText("Error opening the save file");
			erroBox.setContentText("Would you like to select a new directory?");
			ButtonType ok = new ButtonType("Okay");
			ButtonType cancel = new ButtonType("Cancel");

			erroBox.getButtonTypes().setAll(ok, cancel);
			erroBox.showAndWait().ifPresent(response->{
				if(response == ok){
					// Choose a file to save
					FileChooser fileChooser = new FileChooser();
					fileChooser.setTitle("Save As");
					fileChooser.getExtensionFilters().addAll(
						new FileChooser.ExtensionFilter("All Files", "*.*"),
						new FileChooser.ExtensionFilter("JSON", "*.json")
					);
					File file = fileChooser.showSaveDialog(Main.primaryStage);
					if (file != null) {
						workingFileLocation = file.getAbsolutePath();
					}else{
						return;
					}
				}else{
					return;
				}
			});
		}
		Main.changed = false;
		return true;
	}

	public static boolean exit(){
		if(Main.changed){
			// Prompt save option
			Alert saveWindow = new Alert(AlertType.WARNING);
			saveWindow.setTitle(Reference.NAME);
			saveWindow.setHeaderText("Do you want to save any changes made?");
			saveWindow.setContentText("Your changes will be lost you don't save them.");

			ButtonType save = new ButtonType("Save");
			ButtonType dontSave = new ButtonType("Don't Save");
			ButtonType cancel = new ButtonType("Cancel");

			saveWindow.getButtonTypes().setAll(save, dontSave, cancel);
			saveWindow.showAndWait().ifPresent(response->{
				if(response == save){
					if (saveAll()) {
						Platform.exit();
					}
				}else if(response == dontSave){
					Platform.exit();
				}else{

				}
			});
			return false;
		}
		Platform.exit();
		return true;
	}

	public static void main(String[] args){
		EnumUtils.populateAssetTypeMap();
    	EnumUtils.populateCategoryMap();
		launch(args);
	}

	public static boolean isNameTaken(String name){
		for (int i = 0; i < assets.size(); i++) {
			if (assets.getAssetsList().get(i).getName().equals(name)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		Platform.setImplicitExit(false);
		Main.primaryStage = primaryStage;
		primaryStage.setTitle(Reference.NAME);
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>(){
			@Override
			public void handle(WindowEvent e){
				if(!exit()){
					e.consume();
				}
			}
		});
		Pane rootPane = (Pane)FXMLLoader.load(getClass().getResource("fxml_main.fxml"));
		Scene scene = new Scene(rootPane, 1280, 720);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}