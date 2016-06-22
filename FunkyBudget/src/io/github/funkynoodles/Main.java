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

	public static void main(String[] args){
		launch(args);
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

