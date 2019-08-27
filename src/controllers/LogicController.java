package controllers;

import models.Log;
import java.io.File;
import java.util.Calendar;
import java.util.ArrayList;
import java.time.LocalDate;
import java.time.DayOfWeek;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Collections;
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
	private static int maxPages = 1;
	private static double totalFunds;
	private static double totalIncome;
	private static double totalExpenses;
	public static Log logObject;
	public static String loadedListName = "New List";
	public static ArrayList<Log> allLogs = new ArrayList<>();
	public static ArrayList<Log> currentLogs = new ArrayList<>();
	public static HashMap<Integer, Double> yearlyIncome = new HashMap<Integer, Double>();
	public static HashMap<Integer, Double> yearlyExpense = new HashMap<Integer, Double>();
	public static HashMap<Integer, Double> yearlyTotal = new HashMap<Integer, Double>();
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
		
		if (amount.contains("$")){
			amount = amount.replace("$", "");	
		}
		
		if (amount.contains(",")) {
			amount = amount.replace(",", "");
		}
		logObject = new Log(type, date, accFrom, catTo, Double.parseDouble(amount), cnt);
		allLogs.add(logObject);
	}
	
	// ------ Methods for Menu Bar Items ------
	public static void newItemList() {
		loadedListName = "New List";
		allLogs.clear();
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
				out.writeObject(allLogs);
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
				allLogs = null;
				fileIn = new FileInputStream(file);
				in = new ObjectInputStream(fileIn);
				allLogs = (ArrayList<Log>)in.readObject();
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
		return allLogs;
	}
	
	public static void yearlyTotals() {
		double value = 0;
		
		for(Log l : allLogs) {
			int year = l.getDate().getYear();
			
			if(l.getTransactionType()== "Income") {
				if(!yearlyIncome.containsKey(year)) {
					yearlyIncome.put(year, l.getAmount());
				}else {
					value = yearlyIncome.get(year) + l.getAmount();
					yearlyIncome.put(year, value);
				}
				
			}else if(l.getTransactionType()== "Expense") {
				if(!yearlyExpense.containsKey(year)) {
					yearlyExpense.put(year, l.getAmount());
				}else {
					value = yearlyExpense.get(year) + l.getAmount();
					yearlyExpense.put(year, value);
				}
			}
			
			if(!yearlyTotal.containsKey(year)) {
				value = yearlyIncome.get(year) - yearlyExpense.get(year);
				yearlyTotal.put(year, value);
			}else{
				if(l.getTransactionType() == "Income") {
					value = yearlyTotal.get(year) + l.getAmount();
					yearlyTotal.put(year, value);
				}else {
					value = yearlyTotal.get(year) - l.getAmount();
					yearlyTotal.put(year, value);
				}
			}
			
		}
		
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
	
	public static void calculateFunds(String transactionType, double amount) {
		double money = amount;
		if(transactionType.equalsIgnoreCase("Income")) {
			totalIncome += money;
			totalFunds += money;
		} else if(transactionType.equalsIgnoreCase("Expense")) {
			totalExpenses += money;
			totalFunds -= money;
		} else if(transactionType.equalsIgnoreCase("Transfer")) {
			totalExpenses += money;
			totalFunds -= money;
		}
	}
	
	public static void resetFunds() {
		totalIncome = 0;
		totalExpenses = 0;
		totalFunds = 0;
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
	
	private static void updateLabels(int currentLog, Log log) {
		switch(currentLog) {
		case 1:
			firstDay = Integer.toString(log.getDate().getDayOfMonth());
			firstDayName = getDayName(log.getDate());
			firstLabel = log.getLogSummary();
			break;
		case 2:
			secondDay = Integer.toString(log.getDate().getDayOfMonth());
			secondDayName = getDayName(log.getDate());
			secondLabel = log.getLogSummary();
			break;
		case 3:
			thirdDay = Integer.toString(log.getDate().getDayOfMonth());
			thirdDayName = getDayName(log.getDate());
			thirdLabel = log.getLogSummary();
			break;
		case 4:
			fourthDay = Integer.toString(log.getDate().getDayOfMonth());
			fourthDayName = getDayName(log.getDate());
			fourthLabel = log.getLogSummary();
			break;
		case 5:
			fifthDay = Integer.toString(log.getDate().getDayOfMonth());
			fifthDayName = getDayName(log.getDate());
			fifthLabel = log.getLogSummary();
			break;
		}
	}
	
	public static void updateLogListForMonth() {
		clearLabels();
		currentLogs.clear();
		resetFunds();
		
		for(Log l : allLogs) {
			if(l.getDate().getMonthValue() == month && l.getDate().getYear() == year) {
				currentLogs.add(l);
				calculateFunds(l.getTransactionType(), l.getAmount());
			}
		}
		
		maxPages = currentLogs.size() / 5;
		if(currentLogs.size() % 5 > 0) {
			maxPages += 1;
		}
		
		if(maxPages == 0) {
			maxPages = 1;
		}
		
		updatePage();
	}
	
	public static void updatePage() {
		clearLabels();
		
		Collections.sort(currentLogs, new Comparator<Log>() {
			public int compare(Log o1, Log o2) {
				if(o1.getDate() == null || o2.getDate() == null)
					return 0;
				return o1.getDate().compareTo(o2.getDate());
			}
		});

		int test = (page - 1) * 5;
		int logNum = 1;

		for(int i = test; i < test + 5; i++) {
			if(i < currentLogs.size()) {
				updateLabels(logNum, currentLogs.get(i));
				logNum++;
			}
		}

	}
	
	private static void clearLabels() {
		firstDay = "";
		firstDayName = "";
		firstLabel = "";
		secondDay = "";
		secondDayName = "";
		secondLabel = "";
		thirdDay = "";
		thirdDayName = "";
		thirdLabel = "";
		fourthDay = "";
		fourthDayName = "";
		fourthLabel = "";
		fifthDay = "";
		fifthDayName = "";
		fifthLabel = "";
	}
	
}