package com.expenses.extract.accountClient.dao;

public class AccountRequest {
	private String agencyNumber;
	private String accountNumber;
	private String bankNumber;
	private String bankName;
	
	public AccountRequest(String agency, String account, String bankNumber, String bankName) {
		this.agencyNumber = agency;
		this.accountNumber = account;
		this.bankNumber = bankNumber;
		this.bankName = bankName;
	}
	
	public String getAgencyNumber() {
		return agencyNumber;
	}
	public AccountRequest setAgencyNumber(String agencyNumber) {
		this.agencyNumber = agencyNumber;
		return this;
	}
	public String getAccountNumber() {
		return accountNumber;
	}
	public AccountRequest setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
		return this;
	}
	public String getBankNumber() {
		return bankNumber;
	}
	public AccountRequest setBankNumber(String bankNumber) {
		this.bankNumber = bankNumber;
		return this;
	}
	public String getBankName() {
		return bankName;
	}
	public AccountRequest setBankName(String bankName) {
		this.bankName = bankName;
		return this;
	}
}
