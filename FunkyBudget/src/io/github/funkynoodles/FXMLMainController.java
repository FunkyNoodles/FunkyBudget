package io.github.funkynoodles;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class FXMLMainController {

    @FXML
    protected void handleExitAction(final ActionEvent event) {
    	Platform.exit();
    }

    @FXML
    private void handleKeyInput(final InputEvent event)
    {
       if (event instanceof KeyEvent)
       {
          final KeyEvent keyEvent = (KeyEvent) event;
          if (keyEvent.isControlDown() && keyEvent.getCode() == KeyCode.A)
          {
          }
       }
    }
}