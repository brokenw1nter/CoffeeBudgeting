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
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.control.DatePicker;

public class SceneController {

	private static String currentScene = "Main";
	private static String previousScene = "Login";
	private static String previousTab = "NewTransaction";
	
	@FXML private MenuBar menuBar = new MenuBar();
	@FXML private Label monthYearLabel = new Label();
	@FXML private Label incomeLabel = new Label();
	@FXML private Label expensesLabel = new Label();
	@FXML private Label totalLabel = new Label();
	@FXML private Label firstDay = new Label();
	@FXML private Label firstDayName = new Label();
	@FXML private ImageView firstImage = new ImageView();
	@FXML private Label firstLabel = new Label();
	@FXML private Label secondDay = new Label();
	@FXML private Label secondDayName = new Label();
	@FXML private ImageView secondImage = new ImageView();
	@FXML private Label secondLabel = new Label();
	@FXML private Label thirdDay = new Label();
	@FXML private Label thirdDayName = new Label();
	@FXML private ImageView thirdImage = new ImageView();
	@FXML private Label thirdLabel = new Label();
	@FXML private Label fourthDay = new Label();
	@FXML private Label fourthDayName = new Label();
	@FXML private ImageView fourthImage = new ImageView();
	@FXML private Label fourthLabel = new Label();
	@FXML private Label fifthDay = new Label();
	@FXML private Label fifthDayName = new Label();
	@FXML private ImageView fifthImage = new ImageView();
	@FXML private Label fifthLabel = new Label();
	@FXML private Label pageLabel = new Label();
	@FXML private Label transactionLabel = new Label();
	@FXML private Label accountLabel = new Label();
	@FXML private Label categoryLabel = new Label();
	@FXML private DatePicker dateSelected = new DatePicker();
	@FXML private TextField accountFromField = new TextField();
	@FXML private TextField categoryToField = new TextField();
	@FXML private TextField amountField = new TextField();
	@FXML private TextField contentField = new TextField();
	
	// --------- Methods for Buttons ----------
	@FXML
	public void previousMonth(InputEvent event) {
		monthYearLabel.setText(LogicController.previousMonth());
		LogicController.updateLogListForMonth();
		updateDisplay();
	}

	@FXML
	public void nextMonth(InputEvent event) {
		monthYearLabel.setText(LogicController.nextMonth());
		LogicController.updateLogListForMonth();
		updateDisplay();
	}

	@FXML
	public void previousPage(InputEvent event) {
		pageLabel.setText(LogicController.previousPage());
		LogicController.updatePage();
		setShortLogs();
	}

	@FXML
	public void nextPage(InputEvent event) {
		pageLabel.setText(LogicController.nextPage());
		LogicController.updatePage();
		setShortLogs();
	}
	
	@FXML public void newProfile(InputEvent event) {
		
	}

	@FXML
	public void newTransaction(InputEvent event) {
		currentScene = "Expense";
		changeScene("/fxmls/NewTransaction.fxml");
	}

	@FXML
	public void incomeTransaction(InputEvent event) {
		currentScene = "Income";
		if (previousTab == "NewTransaction") {
			transactionLabel.setText("Income");
		} else if (previousTab == "IncomeExpense") {
			previousTab = "NewTransaction";
			transactionLabel.setText("Income");
			accountLabel.setText("Account:");
			categoryLabel.setText("Category:");
		}
	}

	@FXML
	public void expenseTransaction(InputEvent event) {
		currentScene = "Expense";
		if (previousTab == "NewTransaction") {
			transactionLabel.setText("Expense");
		} else if (previousTab == "IncomeExpense") {
			previousTab = "NewTransaction";
			transactionLabel.setText("Expense");
			accountLabel.setText("Account:");
			categoryLabel.setText("Category:");
		}
	}

	@FXML
	public void transferTransaction(InputEvent event) {
		currentScene = "Transfer";
		previousTab = "IncomeExpense";
		transactionLabel.setText("Transfer");
		accountLabel.setText("From:");
		categoryLabel.setText("To:");
	}

	@FXML
	public void cancelTransaction(InputEvent event) {
		currentScene = "Main";
		changeScene("/fxmls/Main.fxml");
	}

	@FXML
	public void confirmTransaction(InputEvent event) {
		LogicController.addTransaction(currentScene, dateSelected.getValue(), accountFromField.getText(),
				categoryToField.getText(), amountField.getText(), contentField.getText());
		LogicController.updateLogListForMonth();
		LogicController.autoSave();
		setShortLogs();
		changeScene("/fxmls/Main.fxml");
	}

	// ------ Methods for Menu Bar Items ------
	@FXML
	public void newList(ActionEvent evnet) {
		LogicController.newItemList();
		updateDisplay();
	}

	@FXML
	public void loadList(ActionEvent event) {
		LogicController.loadItemList();
		LogicController.updateLogListForMonth();
		updateDisplay();
	}

	@FXML
	public void saveList(ActionEvent event) {
		LogicController.saveItemList();
	}
	
	@FXML public void returnScene(ActionEvent event) {
		if(previousScene == "Login") {
			currentScene = "Login";
			changeScene("/fxmls/Login.fxml");
		}
	}
	
	@FXML public void logoutProfile(ActionEvent event) {
		changeScene("/fxmls/Login.fxml");
	}
	
	@FXML public void aboutApplication(ActionEvent event) {
		changeScene("/fxmls/About.fxml");
	}

	@FXML
	public void quitApplication(ActionEvent event) {
		LogicController.autoSave();
		Platform.exit();
	}

	// ------------ Helper Methods ------------
	public void initialize() {
		monthYearLabel.setText(LogicController.getCurrentMonthYear());
		LogicController.autoLoad();
		LogicController.updateLogListForMonth();
		LogicController.updatePage();
		updateDisplay();
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

	public void setTotalFunds() {
		incomeLabel.setTextFill(Color.FORESTGREEN);
		incomeLabel.setText(LogicController.getFormattedFunds("Income"));

		expensesLabel.setTextFill(Color.RED);
		expensesLabel.setText(LogicController.getFormattedFunds("Expenses"));

		String total = LogicController.getFormattedFunds("Total");
		if (total.toCharArray()[0] == '(') {
			total = total.substring(1, total.length() - 1);
			total = "-" + total;
			totalLabel.setTextFill(Color.RED);
		} else if (total.toCharArray()[1] == '0') {
			totalLabel.setTextFill(Color.BLUE);
		} else {
			totalLabel.setTextFill(Color.FORESTGREEN);
		}
		totalLabel.setText(total);
	}

	public void setShortLogs() {
		firstDay.setText(LogicController.firstDay);
		firstDayName.setText(LogicController.firstDayName);
		firstLabel.setText(LogicController.firstLabel);
		secondDay.setText(LogicController.secondDay);
		secondDayName.setText(LogicController.secondDayName);
		secondLabel.setText(LogicController.secondLabel);
		thirdDay.setText(LogicController.thirdDay);
		thirdDayName.setText(LogicController.thirdDayName);
		thirdLabel.setText(LogicController.thirdLabel);
		fourthDay.setText(LogicController.fourthDay);
		fourthDayName.setText(LogicController.fourthDayName);
		fourthLabel.setText(LogicController.fourthLabel);
		fifthDay.setText(LogicController.fifthDay);
		fifthDayName.setText(LogicController.fifthDayName);
		fifthLabel.setText(LogicController.fifthLabel);
	}
	private void updateDisplay() {
		setTotalFunds();
		pageLabel.setText(LogicController.getCurrentMaxPages());
		setShortLogs();
	}
	
}