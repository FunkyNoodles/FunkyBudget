package io.github.funkynoodles;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Main extends Application{

	public static Assets assets = new Assets();

	public static boolean changed = false;

	public static int findAssetIndexByName(String name){
		for (int i = 0; i < assets.size(); i++) {
			if (assets.getAssetsList().get(i).getName() == name) {
				return i;
			}
		}
		return -1;
	}

	public static boolean exit(){
		if(Main.changed){
			// Prompt save option
			Alert saveWindow = new Alert(AlertType.WARNING);
			saveWindow.setTitle("Funky Budget");
			saveWindow.setHeaderText("Do you want to save any changed made?");
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