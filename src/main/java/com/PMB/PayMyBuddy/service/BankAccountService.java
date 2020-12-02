package com.PMB.PayMyBuddy.service;

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

	/**
	 * This method depends on the response of the user's bank API.
	 * 
	 * @param user
	 * @param amountAsked
	 * @return the amountAsked if the response from the bank is true
	 */
	public Double fundAppAccount(User user, Double amountAsked) {
		try {
			if (user.getBankAccount().isResponseFromBankApi()) {
				return amountAsked;
			} else {
				LOGGER.error("not enough money on bank account to get " + amountAsked + ".");
				throw new EmptyBankAccountException("not enough money on bank account.");
			}
		} catch (EmptyBankAccountException e) {
			e.printStackTrace();
		}
		return amountAsked;
	}

	/**
	 * This method depends on the response of the user's bank API.
	 * 
	 * @param user
	 * @param amountToSend
	 */
	public void fundBankAccount(User user, Double amountToSend) {
		if (user.getBankAccount().isResponseFromBankApi()) {
			LOGGER.info("user bank account is granted with amountGiven = " + amountToSend + ".");
		}
	}

	/**
	 * Save a Bank Account
	 * 
	 * @param user
	 * @param bankAccount
	 */
	public void saveBankAccount(User user, BankAccount bankAccount) {
		BankAccount bank = user.getBankAccount();
		if (bank == null) {
			user.setBankAccount(bankAccount);
			bankRepository.save(bankAccount);
			LOGGER.info("bank account saved for user "+user.getEmail());
		} else {
			LOGGER.info("user's bank already exists");
		}
	}

}
