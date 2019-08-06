package controllers;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.application.Platform;

public class SceneController {
	
	// ------ Methods for Menu Bar Items ------
	@FXML
	public void quitApplication(ActionEvent event) {
		Platform.exit();
	}
	
}