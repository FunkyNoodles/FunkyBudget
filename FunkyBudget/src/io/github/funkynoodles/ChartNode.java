package io.github.funkynoodles;

import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

public class ChartNode extends StackPane{

	final private Label label = new Label();

	public ChartNode(Number number, int seriesIndex) {
		label.setText(number.toString());
		label.getStyleClass().addAll("default-color" + Integer.toString(seriesIndex), "chart-line-symbol", "chart-series-line");
		label.setMinSize(Label.USE_PREF_SIZE, Label.USE_PREF_SIZE);
		setMinSize(Label.USE_PREF_SIZE, Label.USE_PREF_SIZE);

	setOnMouseEntered(new EventHandler<MouseEvent>() {
		@Override public void handle(MouseEvent mouseEvent) {
			getChildren().setAll(label);
			setCursor(Cursor.NONE);
			toFront();
		}
	});
	setOnMouseExited(new EventHandler<MouseEvent>() {
		@Override public void handle(MouseEvent mouseEvent) {
			getChildren().clear();
			setCursor(Cursor.CROSSHAIR);
		}
	});
	}

	public void show(){
		getChildren().setAll(label);
	}

	public void hide(){
		getChildren().clear();
	}
}
