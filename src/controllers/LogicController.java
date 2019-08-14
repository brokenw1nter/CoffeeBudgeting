package controllers;

import models.Log;
import java.io.File;
import java.util.Calendar;
import java.util.ArrayList;
import java.time.LocalDate;
import java.time.DayOfWeek;
import java.io.IOException;
import java.text.NumberFormat;
import javax.swing.JOptionPane;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import javax.swing.JFileChooser;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DateFormatSymbols;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileSystemView;
import javax.swing.filechooser.FileNameExtensionFilter;

public class LogicController {
	
	private static int year;
	private static int month;
	private static int page = 1;
	public static int currentLog;
	private static int maxPages = 1;
	private static double totalFunds;
	private static double totalIncome;
	private static double totalExpenses;
	private static String transactionType;
	public static Log logObject;
	public static String loadedListName = "New List";
	public static ArrayList<Log> logs = new ArrayList<>();
	private static NumberFormat fmt = NumberFormat.getCurrencyInstance();
	
	public static String firstDay, firstDayName, firstLabel = null;
	public static String secondDay, secondDayName, secondLabel = null;
	public static String thirdDay, thirdDayName, thirdLabel = null;
	public static String fourthDay, fourthDayName, fourthLabel = null;
	public static String fifthDay, fifthDayName, fifthLabel = null;
	
	// --------- Methods for Buttons ----------
	public static String previousMonth() {
		String value = null;
		if(month == 1) {
			month = 12;
			year--;
			value = getMonthName(month) + " " + year;
		}  else {
			month --;
			value = getMonthName(month) + " " + year;
		}
		return value;
	}
	
	public static String nextMonth() {
		String value = null;
		if(month == 12) {
			month = 1;
			year++;
			value = getMonthName(month) + " " + year;
		} else {
			month ++;
			value = getMonthName(month) + " " + year;
		}
		return value;
	}
	
	public static String previousPage() {
		if(page != 1) {
			page--;
		}
		return "Page: " + page + "/" + maxPages;
	}
	
	public static String nextPage() {
		if(page != maxPages) {
			page++;
		}
		return "Page: " + page + "/" + maxPages;
	}
	
	public static void addTransaction(String type, LocalDate date,
			String accFrom, String catTo, String amount, String cnt) {
		setLogAndPage();
		transactionType = type;
		if(type == "Income") {
			calculateFunds(amount);
			logObject = new Log(type, date, accFrom, catTo,
					Double.parseDouble(amount), cnt);
			logs.add(logObject);
			getStoredLogs();
		} else if(type == "Expense") {
			calculateFunds(amount);
			logObject = new Log(type, date, accFrom, catTo,
					Double.parseDouble(amount), cnt);
			logs.add(logObject);
			getStoredLogs();
		} else if(type == "Transfer") {
			calculateFunds(amount);
		}
	}
	
	// ------ Methods for Menu Bar Items ------
	public static void newItemList() {
		loadedListName = "New List";
		logs.clear();
	}
	
	public static void saveItemList() {
		JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		jfc.setDialogTitle("Choose a Directory");
		jfc.setMultiSelectionEnabled(false);
		jfc.setAcceptAllFileFilterUsed(false);
		FileNameExtensionFilter filter = new FileNameExtensionFilter("CB File", "cb");
		jfc.addChoosableFileFilter(filter);
		
		int returnValue = jfc.showSaveDialog(null);
		if(returnValue == JFileChooser.APPROVE_OPTION) {
			File file = jfc.getSelectedFile();
			FileFilter filefilter = jfc.getFileFilter();
			FileOutputStream fileOut = null;
			ObjectOutputStream out = null;
			
			if(filefilter instanceof FileNameExtensionFilter && ! filefilter.accept(file)) {
				FileNameExtensionFilter fnef = (FileNameExtensionFilter)filefilter;
				String extension = fnef.getExtensions()[0];
				String newName = file.getName() + "." + extension;
				file = new File(file.getParent(), newName);
			}
			
			loadedListName = file.getName();
			loadedListName = loadedListName.substring(0, loadedListName.length() - 5);
			
			try {
				fileOut = new FileOutputStream(file);
				out = new ObjectOutputStream(fileOut);
				out.writeObject(logs);
			} catch(IOException ioe) {
				ioe.printStackTrace();
				System.out.println("IOException has been caught.");
			} finally {
				try {
					out.close();
					fileOut.close();
				} catch (IOException ioe) {
					ioe.printStackTrace();
					System.out.println("IOException has been caught.");
				}
			}
		}
		JOptionPane.showConfirmDialog(null, "Items have been saved.", "Information", JOptionPane.PLAIN_MESSAGE, JOptionPane.INFORMATION_MESSAGE);
	}
	
	@SuppressWarnings("unchecked")
	public static ArrayList<Log> loadItemList() {
		JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		jfc.setDialogTitle("Select a File");
		jfc.setMultiSelectionEnabled(false);
		jfc.setAcceptAllFileFilterUsed(false);
		FileNameExtensionFilter filter = new FileNameExtensionFilter("CB File", "cb");
		jfc.addChoosableFileFilter(filter);
		
		int returnValue = jfc.showOpenDialog(null);
		if(returnValue == JFileChooser.APPROVE_OPTION) {
			File file = jfc.getSelectedFile();
			FileFilter filefilter = jfc.getFileFilter();
			FileInputStream fileIn = null;
			ObjectInputStream in = null;
			
			if(filefilter instanceof FileNameExtensionFilter && ! filefilter.accept(file)) {
				FileNameExtensionFilter fnef = (FileNameExtensionFilter)filefilter;
				String extension = fnef.getExtensions()[0];
				String newName = file.getName() + "." + extension;
				file = new File(file.getParent(), newName);
			}
			
			loadedListName = file.getName();
			loadedListName = loadedListName.substring(0, loadedListName.length() - 5);
			
			try {
				logs = null;
				fileIn = new FileInputStream(file);
				in = new ObjectInputStream(fileIn);
				logs = (ArrayList<Log>)in.readObject();
			} catch(IOException ioe) {
				ioe.printStackTrace();
				System.out.println("IOException has been caught.");
			} catch(ClassNotFoundException cnfe) {
				cnfe.printStackTrace();
				System.out.println("ClassNotFoundException has been caught.");
			} finally {
				try {
					in.close();
					fileIn.close();
				} catch (IOException ioe) {
					ioe.printStackTrace();
					System.out.println("IOException has been caught.");
				}
			}
		}
		JOptionPane.showConfirmDialog(null, "Items have been loaded.", "Information", JOptionPane.PLAIN_MESSAGE, JOptionPane.INFORMATION_MESSAGE);
		return logs;
	}
	
	// ------------ Helper Methods ------------
	public static String getCurrentMonthYear() {
		String value = null;
		Calendar cal = Calendar.getInstance();
		month = cal.get(Calendar.MONTH) + 1;
		year = cal.get(Calendar.YEAR);
		value = getMonthName(month) + " " + year;
		return value;
	}
	
	public static String getMonthName(int monthNumber) {
		return new DateFormatSymbols().getMonths()[monthNumber - 1];
	}
	
	public static String getDayName(LocalDate date) {
		DayOfWeek dayName = date.getDayOfWeek();
		return dayName.toString();
	}
	
	public static String getCurrentMaxPages() {
		return "Page: " + page + "/" + maxPages;
	}
	
	public static void calculateFunds(String amount) {
		double money = 0;
		if(transactionType == "Income") {
			money = Double.parseDouble(amount);
			totalIncome += money;
			totalFunds += money;
		} else if(transactionType == "Expense") {
			money = Double.parseDouble(amount);
			totalExpenses += money;
			totalFunds -= money;
		} else if(transactionType == "Transfer") {
			
		}
	}
	
	public static String getFormattedFunds(String type) {
		String value = null;
		if(type == "Income") {
			value = fmt.format(totalIncome);
		} else if(type == "Expenses") {
			value = fmt.format(totalExpenses);
		} else if(type == "Total") {
			value = fmt.format(totalFunds);
		}
		return value;
	}
	
	public static String getShortenedLog() {
		String value = null;
		if(transactionType == "Income") {
			value = "Received " + fmt.format(logObject.getAmount()) +
					" into " + logObject.getAccountFrom() +
					" from " + logObject.getContent() + " for " +
					logObject.getCategoryTo() + ".";
		} else if(transactionType == "Expense") {
			value = "Purchased " + logObject.getCategoryTo() + " from " +
					logObject.getContent() + " for " +
					fmt.format(logObject.getAmount()) + " from " +
					logObject.getAccountFrom() + ".";
		} else if(transactionType == "Transfer") {
			
		}
		return value;
	}
	
	public static void getStoredLogs() {
		if(currentLog == 1) {
			logs.get(0);
			firstDay = Integer.toString(logObject.getDate().getDayOfMonth());
			firstDayName = getDayName(logObject.getDate());
			firstLabel = getShortenedLog();
		}
		if(currentLog == 2) {
			logs.get(1);
			secondDay = Integer.toString(logObject.getDate().getDayOfMonth());
			secondDayName = getDayName(logObject.getDate());
			secondLabel = getShortenedLog();
		}
		if(currentLog == 3) {
			logs.get(2);
			thirdDay = Integer.toString(logObject.getDate().getDayOfMonth());
			thirdDayName = getDayName(logObject.getDate());
			thirdLabel = getShortenedLog();
		}
		if(currentLog == 4) {
			logs.get(3);
			fourthDay = Integer.toString(logObject.getDate().getDayOfMonth());
			fourthDayName = getDayName(logObject.getDate());
			fourthLabel = getShortenedLog();
		}
		if(currentLog == 5) {
			logs.get(4);
			fifthDay = Integer.toString(logObject.getDate().getDayOfMonth());
			fifthDayName = getDayName(logObject.getDate());
			fifthLabel = getShortenedLog();
		}
	}
	
	public static void setLogAndPage() {
		currentLog++;
		if(currentLog == 6) {
			currentLog = 1;
			maxPages++;
			page++;
		}
	}
	
	public static int convertDateToMonth() {
		int value = 0;
		LocalDate logDate = logObject.getDate();
		value = logDate.getMonthValue();
		return value;
	}
	
	public static int convertDateToYear() {
		int value = 0;
		LocalDate logDate = logObject.getDate();
		value = logDate.getYear();
		return value;
	}
	
	public static void getCurrentLogs() {
		for(int i = 0; i > logs.size(); i++) {
			logs.get(i);
			int logMonth = convertDateToMonth();
			int logYear = convertDateToYear();
			System.out.println("Current Month: " + month + "\nLog Month: " + logMonth
					+ "\nCurrent Year: " + year + "\nLog Year: " + logYear);
			if(logMonth == month && logYear == year) {
				firstDay = Integer.toString(logObject.getDate().getDayOfMonth());
				firstDayName = getDayName(logObject.getDate());
				firstLabel = getShortenedLog();
			}
		}
	}
	
}