package io.github.funkynoodles;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.opencsv.CSVReader;

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
			if (assets.getAssetsList().get(i).getName().equals(name)) {
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
		// Find the first tab, if not the root tab, something's wrong
		Tab rootTab = null;
		for (int i = 0; i < tabPane.getTabs().size(); ++i) {
			rootTab = tabPane.getTabs().get(i);
			if (rootTab.getId().equals("rootTabAsset")) {
				break;
			}
			return false;
		}
		// Close existing tabs
		Tab tab = null;
		while (tabPane.getTabs().size() > 1) {
			tab = tabPane.getTabs().get(1);
			tabPane.getTabs().remove(tab);
		}
		VBox rootTabVBox = (VBox)rootTab.getContent();
		int numberOfExistingFields = rootTabVBox.getChildren().size() - 1;
		// Remove existing account fields
		if (rootTabVBox.getChildren().size() > 2) {
			for (int i = 1; i < numberOfExistingFields; i++) {
				rootTabVBox.getChildren().remove(1);
			}
		}
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
		Main.changed = false;
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
		scene.getStylesheets().add("io/github/funkynoodles/stylesheet.css");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static boolean readCSVfromGnuCash(String fileName){

		Scene rootScene = primaryStage.getScene();
		VBox assetVBox = (VBox)rootScene.lookup("#assetVBox");
		TabPane tabPane = (TabPane) rootScene.lookup("#tabPane");

		FileReader file = null;

		try {
			file = new FileReader(fileName);
		} catch (FileNotFoundException e) {
			return false;
		}
		CSVReader reader = new CSVReader(file);
		String[] nextLine;
		int lineNum = 0;
		try {
			while((nextLine = reader.readNext()) != null){
				++lineNum;
				if (lineNum % 3 != 2) {
					continue;
				}
				//System.out.println(nextLine[1]);
				String dateText = nextLine[0];
				String assetName = nextLine[1];
				String detail = nextLine[3];
				String catString = nextLine[6];
				String toString = nextLine[12];
				String fromString = nextLine[13];
				Asset asset = null;
				int idx = findAssetIndexByName(assetName);

				if (idx < 0) {
					asset = new Asset(assetName, AssetType.BANK_CHECKINGS);
					assets.insert(asset);

					HBox assetField = (HBox)FXMLLoader.load(Main.class.getResource("fxml_account_field.fxml"));
		    		((Label)assetField.getChildren().get(0)).setText(assetName);
		    		((TextField)assetField.getChildren().get(1)).setText("0.00");
					assetVBox.getChildren().add(assetVBox.getChildren().size() - 1, assetField);
				}else{
					asset = assets.getAssetsList().get(idx);
				}
				LocalDate date = LocalDate.parse(dateText, DateTimeFormatter.ofPattern("MM/dd/yyyy"));

				toString = toString.replace(",", "");
				if (toString.contains("(")) {
					toString = toString.replace("(", "");
					toString = toString.replace(")", "");
					toString = "-" + toString;
				}
				fromString = fromString.replace(",", "");
				if (fromString.contains("(")) {
					fromString = fromString.replace("(", "");
					fromString = fromString.replace(")", "");
					fromString = "-" + fromString;
				}

				double toNum = 0;
				if (!toString.equals("")) {
					toNum = Double.parseDouble(toString);
				}
				double fromNum = 0;
				if (!fromString.equals("")) {
					fromNum = Double.parseDouble(toString);
				}
				//System.out.println(toNum);
				TransferField tf = new TransferField(date, toNum - fromNum, detail, Category.EXPENSE_AUTO_FEES);
				asset.insert(tf);
				// C:/Users/Louis/Desktop/000000.csv
			}
			// Update balance on home tab and top
			Tab tabAsset = null;
			for (int i = 0; i < tabPane.getTabs().size(); i++) {
				if(tabPane.getTabs().get(i).getId().compareTo("rootTabAsset") == 0){
					tabAsset = tabPane.getTabs().get(i);
					break;
				}
			}
			VBox rootTabVBox = (VBox)tabAsset.getContent();
			HBox accountFieldHBox = null;
			Label accountFieldLabel = null;
			TextField accountFieldTextField = null;

			for (int i = 1; i < rootTabVBox.getChildren().size() - 1; i++) {
				accountFieldHBox = (HBox)rootTabVBox.getChildren().get(i);
				accountFieldLabel = (Label)accountFieldHBox.getChildren().get(0);
				String assetName = accountFieldLabel.getText();
				Asset asset = assets.getAssetsList().get(findAssetIndexByName(assetName));
				asset.updateBalance();
				accountFieldTextField = (TextField) accountFieldHBox.getChildren().get(1);
				accountFieldTextField.setText(asset.getBalanceStr());
			}
			// The only way to update the GUI I can think of
			// Maybe use runlater, Task, or another thread to do this function
			primaryStage.hide();
			primaryStage.show();
		} catch (IOException e) {
			return false;
		}
		return true;
	}

	public static class ReadGnuCashRunnable implements Runnable {

		String fileName;
		public ReadGnuCashRunnable(String fileName){
			this.fileName = fileName;
		}
	    public void run(){

	    }
	  }
}