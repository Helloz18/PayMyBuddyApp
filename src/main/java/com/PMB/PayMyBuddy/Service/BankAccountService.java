package com.PMB.PayMyBuddy.Service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.PMB.PayMyBuddy.model.User;
import com.PMB.PayMyBuddy.repository.bankAccountRepository;

@Service
public class BankAccountService {
	
	final Logger LOGGER = LoggerFactory.getLogger(BankAccountService.class);	
	
	
	@Value("${amountMax}")
	private Double amountMax;

	@Autowired
	bankAccountRepository bankRepository;
	
	@Autowired
	UserService userService;

	public void fundAppAccount(User user, Double amountAsked) {
		if(user.getBankAccount().isAccountHasMoney()) {
			Double amountdepart = user.getAppAccount();
			Double amountResult = amountdepart+amountAsked;
			if(amountResult <= amountMax) {
				user.setAppAccount(amountResult);
				userService.save(user);
			}else {
				LOGGER.error("quota reached, the amountMax="+amountMax+" will be reached.");				
			}
		}
		else {
			LOGGER.error("not enough money on bank account.");			
		}
	}
	
	public void fundBankAccount(User user, Double amountGiven) {
		Double amountUserOnApp = user.getAppAccount();
		if(amountUserOnApp >= amountGiven) {
			LOGGER.info("user bank account is granted with amountGiven = "+amountGiven+".");
			Double amountResult = amountUserOnApp - amountGiven;
			user.setAppAccount(amountResult);
			userService.save(user);
			LOGGER.info("user appAccount is now = " + amountResult+".");
		}else {
			LOGGER.error("Trying to transfer more money than is present on app account.");
		}
	}
	
}
