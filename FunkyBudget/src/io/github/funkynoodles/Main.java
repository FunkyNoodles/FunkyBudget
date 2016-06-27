package io.github.funkynoodles;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import com.google.gson.Gson;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Main extends Application{

	public static Assets assets = new Assets();

	public static boolean changed = false;

	public static String workingFileLocation = "";

	public static int findAssetIndexByName(String name){
		for (int i = 0; i < assets.size(); i++) {
			if (assets.getAssetsList().get(i).getName() == name) {
				return i;
			}
		}
		return -1;
	}

	public static void saveAll(){
		Gson gson = new Gson();
		//String str = gson.toJson(assets);
		if (workingFileLocation.equals("")) {
			// Choose a file to save
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Save As");
			fileChooser.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("All Files", "*.*"),
				new FileChooser.ExtensionFilter("JSON", "*.json")
			);
			File file = fileChooser.showSaveDialog(null);
			if (filSystem.out.println(str);e != null) {
				workingFileLocation = file.getAbsolutePath();
			}else{
				return;
			}
		}
		try {
			Writer writer = new FileWriter(workingFileLocation);
			gson.toJson(assets, writer);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//System.out.println(str);
	}

	public static boolean exit(){
		if(Main.changed){
			// Prompt save option
			Alert saveWindow = new Alert(AlertType.WARNING);
			saveWindow.setTitle("Funky Budget");
			saveWindow.setHeaderText("Do you want to save any changes made?");
			saveWindow.setContentText("Your changes will be lost you don't save them.");

			ButtonType save = new ButtonType("Save");
			ButtonType dontSave = new ButtonType("Don't Save");
			ButtonType cancel = new ButtonType("Cancel");

			saveWindow.getButtonTypes().setAll(save, dontSave, cancel);
			saveWindow.showAndWait().ifPresent(response->{
				if(response == save){
					// TODO save function
					Platform.exit();
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
		primaryStage.setTitle("Funky Budget");
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