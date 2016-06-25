package io.github.funkynoodles;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application{

	public static Assets assets = new Assets();
	public static void main(String[] args){
		launch(args);
	}

	public static int findAssetIndexByName(String name){
		for (int i = 0; i < assets.size(); i++) {
			if (assets.getAssetsList().get(i).getName() == name) {
				return i;
			}
		}
		return -1;
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
		primaryStage.setTitle("Funky Budget");
		Pane rootPane = (Pane)FXMLLoader.load(getClass().getResource("fxml_main.fxml"));
		Scene scene = new Scene(rootPane, 1280, 720);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}