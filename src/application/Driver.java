package application;

import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Parent;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.stage.StageStyle;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.application.Application;

public class Driver extends Application {

	private double xOffset = 0;
	private double yOffset = 0;

	@Override
	public void start(Stage stage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/FXMLs/Main.fxml"));
			stage.setTitle("Coffee Budgeting");
			stage.setResizable(false);
			stage.initStyle(StageStyle.UNDECORATED);

			// Grabs the Root
			root.setOnMousePressed(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					xOffset = event.getSceneX();
					yOffset = event.getSceneY();
				}
			});

			// Makes the Window Movable
			root.setOnMouseDragged(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					stage.setX(event.getScreenX() - xOffset);
					stage.setY(event.getScreenY() - yOffset);
				}
			});

			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/fxmls/Main.css").toExternalForm());
			stage.setScene(scene);
			stage.show();
		} catch (IOException ioe) {
			ioe.printStackTrace();
			System.out.println("IOException has been caught.");
		}
	}

	public static void main(String[] args) {
		launch(args);
	}

}