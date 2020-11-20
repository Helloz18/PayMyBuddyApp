package com.PMB.PayMyBuddy.Service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.PMB.PayMyBuddy.exception.EmptyBankAccountException;
import com.PMB.PayMyBuddy.model.BankAccount;
import com.PMB.PayMyBuddy.model.User;
import com.PMB.PayMyBuddy.repository.BankAccountRepository;

@Service
@Transactional
public class BankAccountService {
	
	final Logger LOGGER = LoggerFactory.getLogger(BankAccountService.class);	
	

	@Autowired
	BankAccountRepository bankRepository;
	
	@Autowired
	UserService userService;

	public Double fundAppAccount(User user, Double amountAsked)  {
		try {
			if(user.getBankAccount().isResponseFromBankApi()) {
				amountAsked = amountAsked;
			}else {
				LOGGER.error("not enough money on bank account to get "+amountAsked +".");
				throw new EmptyBankAccountException("not enough money on bank account.");
			}
		} catch (EmptyBankAccountException e) {
			e.printStackTrace();
		}
		
		return amountAsked;
	}
	
	public void fundBankAccount(User user, Double amountToSend) {
		LOGGER.info("user bank account is granted with amountGiven = "+amountToSend+".");		
	}
	
	public void saveBankAccount(User user, BankAccount bankAccount) {
		BankAccount bank = user.getBankAccount();
		if(bank == null) {
			user.setBankAccount(bankAccount);
			bankRepository.save(bankAccount);
		}
	}
	
}
