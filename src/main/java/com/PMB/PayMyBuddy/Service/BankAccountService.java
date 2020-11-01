package com.PMB.PayMyBuddy.Service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.PMB.PayMyBuddy.exception.EmptyBankAccountException;
import com.PMB.PayMyBuddy.exception.NotEnoughMoneyException;
import com.PMB.PayMyBuddy.model.BankAccount;
import com.PMB.PayMyBuddy.model.User;
import com.PMB.PayMyBuddy.repository.bankAccountRepository;

@Service
@Transactional
public class BankAccountService {
	
	final Logger LOGGER = LoggerFactory.getLogger(BankAccountService.class);	
	

	@Autowired
	bankAccountRepository bankRepository;
	
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
	
	public void fundBankAccount(User user, Double amountGiven) {
		try {
			Double amountUserOnApp = user.getAppAccount();
			if(amountUserOnApp >= amountGiven) {
				LOGGER.info("user bank account is granted with amountGiven = "+amountGiven+".");
				Double amountResult = amountUserOnApp - amountGiven;
				user.setAppAccount(amountResult);
				userService.save(user);
				LOGGER.info("user appAccount is now = " + amountResult+".");
			}else {
				LOGGER.error("Trying to transfer more money than is present on app account.");
				throw new NotEnoughMoneyException("not enough money on app account");
			}
		} catch (NotEnoughMoneyException e) {
			e.printStackTrace();
		}
	}
	
	public void save(BankAccount bankAccount) {
		bankRepository.save(bankAccount);
	}
	
}
