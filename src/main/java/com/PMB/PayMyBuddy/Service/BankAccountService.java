package com.PMB.PayMyBuddy.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.PMB.PayMyBuddy.model.BankAccount;
import com.PMB.PayMyBuddy.model.User;
import com.PMB.PayMyBuddy.repository.bankAccountRepository;

@Service
public class BankAccountService {

	@Autowired
	bankAccountRepository bankRepository;
	
	public BankAccount get(int id) {
		return bankRepository.findById(id).get();
	}
	
	public String fundAppAccount(User user) {
		BankAccount userBank = user.getBankAccount();
		String response="";
		Boolean bankValidateTransfer = bankValidationTransfer(userBank);
		if (bankValidateTransfer = false) {
			response= "no money on this account, tranfer refused";
		}
		else if(bankValidateTransfer = true) {
			response= "transfer accepted";
		}
		return response;
	}
	
	public Boolean bankValidationTransfer(BankAccount userBank) {
		
		Boolean bankValidation = false;
		return bankValidation;
		
	}
}
