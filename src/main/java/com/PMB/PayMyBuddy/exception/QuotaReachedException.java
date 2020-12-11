package com.PMB.PayMyBuddy.exception;

public class QuotaReachedException extends Exception {
	
	public QuotaReachedException(String errorMessage) {
		super(errorMessage);
	}

}
