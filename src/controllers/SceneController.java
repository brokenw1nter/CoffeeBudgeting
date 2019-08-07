package controllers;

import javafx.fxml.FXML;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.event.ActionEvent;
import javafx.scene.paint.Color;
import javafx.scene.control.Label;
import javafx.application.Platform;
import javafx.scene.control.MenuBar;
import javafx.scene.input.InputEvent;

public class SceneController {
	
	private static String previousScene = "Main";
	private static String previousTab = "NewTransaction";
	
	@FXML private MenuBar menuBar = new MenuBar();
	@FXML private Label monthYearLabel = new Label();
	@FXML private Label incomeLabel = new Label();
	@FXML private Label expensesLabel = new Label();
	@FXML private Label totalLabel = new Label();
	@FXML private Label firstDay = new Label();
	@FXML private Label firstDayName = new Label();
	@FXML private Label firstLabel = new Label();
	@FXML private Label secondDay = new Label();
	@FXML private Label secondDayName = new Label();
	@FXML private Label secondLabel = new Label();
	@FXML private Label thirdDay = new Label();
	@FXML private Label thirdDayName = new Label();
	@FXML private Label thirdLabel = new Label();
	@FXML private Label fourthDay = new Label();
	@FXML private Label fourthDayName = new Label();
	@FXML private Label fourthLabel = new Label();
	@FXML private Label fifthDay = new Label();
	@FXML private Label fifthDayName = new Label();
	@FXML private Label fifthLabel = new Label();
	@FXML private Label pageLabel = new Label();
	@FXML private Label transactionLabel = new Label();
	@FXML private Label accountLabel = new Label();
	@FXML private Label categoryLabel = new Label();
	
	// --------- Methods for Buttons ----------
	@FXML public void previousMonth(InputEvent event) {
		monthYearLabel.setText(LogicController.previousMonth());
	}
	
	@FXML public void nextMonth(InputEvent event) {
		monthYearLabel.setText(LogicController.nextMonth());
	}
	
	@FXML public void previousPage(InputEvent event) {
		pageLabel.setText(LogicController.previousPage());
	}
	
	@FXML public void nextPage(InputEvent event) {
		pageLabel.setText(LogicController.nextPage());
	}
	
	@FXML public void newTransaction(InputEvent event) {
		changeScene("/fxmls/NewTransaction.fxml");
	}
	
	@FXML public void incomeTransaction(InputEvent event) {
		if(previousTab == "NewTransaction") {
			transactionLabel.setText("Income");
		} else if(previousTab == "IncomeExpense") {
			previousTab = "NewTransaction";
			transactionLabel.setText("Income");
			accountLabel.setText("Account:");
			categoryLabel.setText("Category:");
		}
	}
	
	@FXML public void expenseTransaction(InputEvent event) {
		if(previousTab == "NewTransaction") {
			transactionLabel.setText("Expense");
		} else if(previousTab == "IncomeExpense") {
			previousTab = "NewTransaction";
			transactionLabel.setText("Expense");
			accountLabel.setText("Account:");
			categoryLabel.setText("Category:");
		}
	}
	
	@FXML public void transferTransaction(InputEvent event) {
		previousTab = "IncomeExpense";
		transactionLabel.setText("Transfer");
		accountLabel.setText("From:");
		categoryLabel.setText("To:");
	}
	
	@FXML public void cancelTransaction(InputEvent event) {
		clearTextFields();
		changeScene("/fxmls/Main.fxml");
	}
	
	@FXML public void confirmTransaction(InputEvent event) {
		LogicController.addTransaction();
		clearTextFields();
		changeScene("/fxmls/Main.fxml");
	}
	
	// ------ Methods for Menu Bar Items ------
	@FXML public void returnScene(ActionEvent event) {
		if(previousScene == "Main") {
			changeScene("/fxmls/Main.fxml");
		}
	}
	
	@FXML public void aboutApplication(ActionEvent event) {
		changeScene("/fxmls/About.fxml");
	}
	
	@FXML public void quitApplication(ActionEvent event) {
		Platform.exit();
	}
	
	// ------------ Helper Methods ------------
	public void initialize() {
		monthYearLabel.setText(LogicController.getCurrentMonthYear());
		incomeLabel.setTextFill(Color.BLUE);
		incomeLabel.setText("$25.00");
		expensesLabel.setTextFill(Color.RED);
		expensesLabel.setText("$54.48");
		totalLabel.setText("$70.52");
		pageLabel.setText(LogicController.getCurrentMaxPages());
	}
	
	public void changeScene(String path) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
		try {
			loader.load();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("IOException has been caught.");
		} finally {
			menuBar.getScene().setRoot(loader.getRoot());
		}
	}
	
	public void clearTextFields() {
		
	}
	
}