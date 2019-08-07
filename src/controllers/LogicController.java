package controllers;

import java.util.Calendar;
import java.text.NumberFormat;
import java.text.DateFormatSymbols;

public class LogicController {
	
	private static int year;
	private static int page = 1;
	private static int monthNumber;
	private static int maxPages = 2;
	private static String monthName;
	private static String monthYearLabel;
	private static double money;
	private static double totalFunds;
	private static double totalIncome;
	private static double totalExpenses;
	private static NumberFormat fmt = NumberFormat.getCurrencyInstance();
	
	// --------- Methods for Buttons ----------
	public static String previousMonth() {
		if(monthNumber == 1) {
			monthNumber = 12;
			monthName = getMonthName(monthNumber);
			year--;
			monthYearLabel = monthName + " " + year;
		}  else {
			monthNumber --;
			monthName = getMonthName(monthNumber);
			monthYearLabel = monthName + " " + year;
		}
		return monthYearLabel;
	}
	
	public static String nextMonth() {
		if(monthNumber == 12) {
			monthNumber = 1;
			monthName = getMonthName(monthNumber);
			year++;
			monthYearLabel = monthName + " " + year;
		} else {
			monthNumber ++;
			monthName = getMonthName(monthNumber);
			monthYearLabel = monthName + " " + year;
		}
		return monthYearLabel;
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
	
	public static void addTransaction() {
		System.out.println("Add Transaction is Working");
	}
	
	// ------------ Helper Methods ------------
	public static String getCurrentMonthYear() {
		Calendar cal = Calendar.getInstance();
		monthNumber = cal.get(Calendar.MONTH) + 1;
		monthName = getMonthName(monthNumber);
		year = cal.get(Calendar.YEAR);
		monthYearLabel = monthName + " " + year;
		return monthYearLabel;
	}
	
	public static String getMonthName(int monthNumber) {
		return new DateFormatSymbols().getMonths()[monthNumber - 1];
	}
	
	public static String getCurrentMaxPages() {
		return "Page: " + page + "/" + maxPages;
	}
	
	public static String getFormattedTotal(double currentTotal, int amount) {
		currentTotal += amount;
		String total = fmt.format(currentTotal);
		return total;
	}
	
}