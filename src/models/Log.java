package models;

import java.time.LocalDate;
import java.io.Serializable;

public class Log implements Serializable {
	
	private String transactionType;
	private LocalDate date;
	private String accountFrom;
	private String categoryTo;
	private double amount;
	private String content;
	private static final long serialVersionUID = 3147967456736229355L;
	
	public Log(String transactionType, LocalDate date, String accountFrom,
			String categoryTo, double amount, String content) {
		setTransactionType(transactionType);
		setDate(date);
		setAccountFrom(accountFrom);
		setCategoryTo(categoryTo);
		setAmount(amount);
		setContent(content);
	}
	
	public String getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
	
	public LocalDate getDate() {
		return date;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}
	
	public String getAccountFrom() {
		return accountFrom;
	}
	public void setAccountFrom(String accountFrom) {
		this.accountFrom = accountFrom;
	}
	
	public String getCategoryTo() {
		return categoryTo;
	}
	public void setCategoryTo(String categoryTo) {
		this.categoryTo = categoryTo;
	}
	
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("Transaction Type: ").append(getTransactionType()).append("\n");
		sb.append("Date: ").append(getDate()).append("\n");
		sb.append("Account/From: ").append(getAccountFrom()).append("\n");
		sb.append("Category/To: ").append(getCategoryTo()).append("\n");
		sb.append("Amount: $").append(getAmount()).append("\n");
		sb.append("Content: ").append(getContent());
		
		return sb.toString();
	}
	
}