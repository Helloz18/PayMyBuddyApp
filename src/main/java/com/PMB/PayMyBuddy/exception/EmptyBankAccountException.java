package com.PMB.PayMyBuddy.exception;

public class EmptyBankAccountException extends Exception {
	
	public EmptyBankAccountException(String errorMessage) {
		super(errorMessage);
	}

}
