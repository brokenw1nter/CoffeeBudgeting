package controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DateFormatSymbols;
import java.text.NumberFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

import models.Log;

public class LogicController {

	private static int year;
	private static int month;
	private static int page = 1;
	private static int maxPages = 1;
	private static int profilePage = 1;
	private static int maxProfilePages = 1;
	private static double totalFunds;
	private static double totalIncome;
	private static double totalExpenses;
	private static String logFolderPath = "..\\Logs\\";
	private static String currentProfileName = "default";
	private static ArrayList<String> profiles = new ArrayList<>();
	public static Log logObject;
	public static String loadedListName = "New List";
	public static ArrayList<Log> allLogs = new ArrayList<>();
	public static ArrayList<Log> currentLogs = new ArrayList<>();
	private static NumberFormat fmt = NumberFormat.getCurrencyInstance();

	public static String firstDay, firstDayName, firstLabel = null;
	public static String secondDay, secondDayName, secondLabel = null;
	public static String thirdDay, thirdDayName, thirdLabel = null;
	public static String fourthDay, fourthDayName, fourthLabel = null;
	public static String fifthDay, fifthDayName, fifthLabel = null;

	public static String firstProfileLabel, secondProfileLabel, thirdProfileLabel, fourthProfileLabel,
			fifthProfileLabel = "";

	// --------- Methods for Buttons ----------
	public static String previousMonth() {
		String value = null;
		if (month == 1) {
			month = 12;
			year--;
			value = getMonthName(month) + " " + year;
		} else {
			month--;
			value = getMonthName(month) + " " + year;
		}
		return value;
	}

	public static String nextMonth() {
		String value = null;
		if (month == 12) {
			month = 1;
			year++;
			value = getMonthName(month) + " " + year;
		} else {
			month++;
			value = getMonthName(month) + " " + year;
		}
		return value;
	}

	public static String previousPage() {
		if (page != 1) {
			page--;
		}
		return "Page: " + page + "/" + maxPages;
	}

	public static String nextPage() {
		if (page != maxPages) {
			page++;
		}
		return "Page: " + page + "/" + maxPages;
	}

	public static String nextProfilePage() {
		if (profilePage != maxProfilePages) {
			profilePage++;
		}
		return "Page: " + profilePage + "/" + maxProfilePages;
	}

	public static String previousProfilePage() {
		if (profilePage != 1) {
			profilePage--;
		}
		return "Page: " + profilePage + "/" + maxProfilePages;
	}

	public static void addTransaction(String type, LocalDate date, String accFrom, String catTo, String amount,
			String cnt) {
		logObject = new Log(type, date, accFrom, catTo, Double.parseDouble(amount), cnt);
		allLogs.add(logObject);
	}

	// ------ Methods for Menu Bar Items ------
	public static void newItemList() {
		loadedListName = "New List";
		allLogs.clear();
		updateLogListForMonth();
		autoSave();
	}

	public static void saveItemList() {
		JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		jfc.setDialogTitle("Choose a Directory");
		jfc.setMultiSelectionEnabled(false);
		jfc.setAcceptAllFileFilterUsed(false);
		FileNameExtensionFilter filter = new FileNameExtensionFilter("CB File", "cb");
		jfc.addChoosableFileFilter(filter);

		int returnValue = jfc.showSaveDialog(null);
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			File file = jfc.getSelectedFile();
			FileFilter filefilter = jfc.getFileFilter();
			FileOutputStream fileOut = null;
			ObjectOutputStream out = null;

			if (filefilter instanceof FileNameExtensionFilter && !filefilter.accept(file)) {
				FileNameExtensionFilter fnef = (FileNameExtensionFilter) filefilter;
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
			} catch (IOException ioe) {
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
		JOptionPane.showConfirmDialog(null, "Items have been saved.", "Information", JOptionPane.PLAIN_MESSAGE,
				JOptionPane.INFORMATION_MESSAGE);
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
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			File file = jfc.getSelectedFile();
			FileFilter filefilter = jfc.getFileFilter();
			FileInputStream fileIn = null;
			ObjectInputStream in = null;

			if (filefilter instanceof FileNameExtensionFilter && !filefilter.accept(file)) {
				FileNameExtensionFilter fnef = (FileNameExtensionFilter) filefilter;
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
				allLogs = (ArrayList<Log>) in.readObject();
			} catch (IOException ioe) {
				ioe.printStackTrace();
				System.out.println("IOException has been caught.");
			} catch (ClassNotFoundException cnfe) {
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
		JOptionPane.showConfirmDialog(null, "Items have been loaded.", "Information", JOptionPane.PLAIN_MESSAGE,
				JOptionPane.INFORMATION_MESSAGE);
		return allLogs;
	}

	// ------------ Load/Save Methods ------------
	@SuppressWarnings("unchecked")
	public static void autoLoad() {
		File dir = new File(logFolderPath);
		File file = new File(logFolderPath + currentProfileName + ".cb");
		FileInputStream fileIn = null;
		ObjectInputStream in = null;

		if (dir.exists() && file.exists()) {
			allLogs.clear();
			if (file.length() != 0) {
				try {
					fileIn = new FileInputStream(file);
					in = new ObjectInputStream(fileIn);
					allLogs = (ArrayList<Log>) in.readObject();
				} catch (IOException ioe) {
					ioe.printStackTrace();
					System.out.println("IOException has been caught.");
				} catch (ClassNotFoundException cnfe) {
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
		} else if (!dir.exists()) {
			dir.mkdir();
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void autoSave() {
		File file = new File(logFolderPath + currentProfileName + ".cb");
		FileOutputStream fileOut = null;
		ObjectOutputStream out = null;

		try {
			fileOut = new FileOutputStream(file);
			out = new ObjectOutputStream(fileOut);
			out.writeObject(allLogs);
		} catch (IOException ioe) {
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

	public static void createNewProfile(String profileName) {
		File dir = new File(logFolderPath);
		File file = new File(logFolderPath + profileName + ".cb");
		if (dir.exists() && !file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void selectProfile(String profileName) {
		currentProfileName = profileName;
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

	public static String getCurrentProfileMaxPages() {
		return "Page: " + profilePage + "/" + maxProfilePages;
	}

	public static void calculateFunds(String transactionType, double amount) {
		double money = amount;
		if (transactionType.equalsIgnoreCase("Income")) {
			totalIncome += money;
			totalFunds += money;
		} else if (transactionType.equalsIgnoreCase("Expense")) {
			totalExpenses += money;
			totalFunds -= money;
		} else if (transactionType.equalsIgnoreCase("Transfer")) {
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
		if (type == "Income") {
			value = fmt.format(totalIncome);
		} else if (type == "Expenses") {
			value = fmt.format(totalExpenses);
		} else if (type == "Total") {
			value = fmt.format(totalFunds);
		}
		return value;
	}

	private static void updateLabels(int currentLog, Log log) {
		if (currentLog == 1) {
			firstDay = Integer.toString(log.getDate().getDayOfMonth());
			firstDayName = getDayName(log.getDate());
			firstLabel = log.getLogSummary();
		}
		if (currentLog == 2) {
			secondDay = Integer.toString(log.getDate().getDayOfMonth());
			secondDayName = getDayName(log.getDate());
			secondLabel = log.getLogSummary();
		}
		if (currentLog == 3) {
			thirdDay = Integer.toString(log.getDate().getDayOfMonth());
			thirdDayName = getDayName(log.getDate());
			thirdLabel = log.getLogSummary();
		}
		if (currentLog == 4) {
			fourthDay = Integer.toString(log.getDate().getDayOfMonth());
			fourthDayName = getDayName(log.getDate());
			fourthLabel = log.getLogSummary();
		}
		if (currentLog == 5) {
			fifthDay = Integer.toString(log.getDate().getDayOfMonth());
			fifthDayName = getDayName(log.getDate());
			fifthLabel = log.getLogSummary();
		}
	}

	public static void updateLogListForMonth() {
		page = 1;
		clearLabels();
		currentLogs.clear();
		resetFunds();

		for (Log l : allLogs) {
			if (l.getDate().getMonthValue() == month && l.getDate().getYear() == year) {
				currentLogs.add(l);
				calculateFunds(l.getTransactionType(), l.getAmount());
			}
		}

		maxPages = currentLogs.size() / 5;
		if (currentLogs.size() % 5 > 0) {
			maxPages += 1;
		}

		if (maxPages == 0) {
			maxPages = 1;
		}

		updatePage();
	}

	public static void updatePage() {
		clearLabels();

		Collections.sort(currentLogs, new Comparator<Log>() {
			public int compare(Log o1, Log o2) {
				if (o1.getDate() == null || o2.getDate() == null)
					return 0;
				return o1.getDate().compareTo(o2.getDate());
			}
		});

		int test = (page - 1) * 5;
		int logNum = 1;

		for (int i = test; i < test + 5; i++) {
			if (i < currentLogs.size()) {
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

	private static void clearProfileLabels() {
		firstProfileLabel = "";
		secondProfileLabel = "";
		thirdProfileLabel = "";
		fourthProfileLabel = "";
		fifthProfileLabel = "";
	}

	public static void getProfilesFromDir() {
		File dir = new File(logFolderPath);
		File[] filesInDir = dir.listFiles();
		profiles.clear();
		profilePage = 1;

		for (File file : filesInDir) {
			String filename = file.getName().substring(0, file.getName().length() - 3);
			profiles.add(filename);
		}

		maxProfilePages = profiles.size() / 5;
		if (profiles.size() % 5 > 0) {
			maxProfilePages += 1;
		}

		if (maxProfilePages == 0) {
			maxProfilePages = 1;
		}

		updateProfilesForPage();
	}

	public static void updateProfilesForPage() {
		clearProfileLabels();

		int test = (profilePage - 1) * 5;
		int logNum = 1;

		for (int i = test; i < test + 5; i++) {
			if (i < profiles.size()) {
				setProfileLabels(profiles.get(i), logNum);
				logNum++;
			}
		}
	}

	public static void setProfileLabels(String profile, int profileNum) {
		switch (profileNum) {
		case 1:
			if (profile != null && !profile.isEmpty()) {
				firstProfileLabel = profile;
			}
			break;
		case 2:
			if (profile != null && !profile.isEmpty()) {
				secondProfileLabel = profile;
			}
			break;
		case 3:
			if (profile != null && !profile.isEmpty()) {
				thirdProfileLabel = profile;
			}
			break;
		case 4:
			if (profile != null && !profile.isEmpty()) {
				fourthProfileLabel = profile;
			}
			break;
		case 5:
			if (profile != null && !profile.isEmpty()) {
				fifthProfileLabel = profile;
			}
			break;
		default:
			System.out.println("test");
		}
	}

}