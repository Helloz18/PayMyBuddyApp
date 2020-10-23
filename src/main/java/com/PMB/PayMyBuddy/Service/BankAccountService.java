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
	
	
	
	
}
