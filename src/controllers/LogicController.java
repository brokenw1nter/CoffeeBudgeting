package controllers;

import java.util.Calendar;
import java.text.NumberFormat;
import java.text.DateFormatSymbols;

public class LogicController {
	
	private static int page;
	private static int year;
	private static int monthNumber;
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
	
	public static String getIncomeTotal(int amount) {
		totalIncome += amount;
		String incomeTotalLabel = fmt.format(totalIncome);
		return incomeTotalLabel;
	}
	
	public static String getExpensesTotal(int amount) {
		totalExpenses += amount;
		String expensesTotalLabel = fmt.format(totalExpenses);
		return expensesTotalLabel;
	}
	
	public static String getMoneyTotal(int amount) {
		totalFunds += amount;
		String totalLabel = fmt.format(totalFunds);
		return totalLabel;
	}
	
}