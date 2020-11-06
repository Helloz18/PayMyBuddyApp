package com.PMB.PayMyBuddy.Service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.PMB.PayMyBuddy.exception.NotEnoughMoneyException;
import com.PMB.PayMyBuddy.exception.QuotaReachedException;
import com.PMB.PayMyBuddy.model.MoneyTransfer;
import com.PMB.PayMyBuddy.model.TypeOfTransfer;
import com.PMB.PayMyBuddy.model.User;
import com.PMB.PayMyBuddy.repository.MoneyTransferRepository;

@Service
@Transactional
public class MoneyTransferService {
	
	final Logger LOGGER = LoggerFactory.getLogger(MoneyTransferService.class);	
	
	@Value("${amountMax}")
	private Double amountMax;
	
	@Autowired
	MoneyTransferRepository moneyTransferRepository;
	
	@Autowired
	TypeOfTransferService typeService;
	
	@Autowired
	BankAccountService bankService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	TypeOfTransferService typeOfTransferService;
	
	/**
	 * This method will get money from bank, then set info for the transfer,
	 * then save the transfer in DB (the user is not saved, the call of this method
	 * should be in userService)
	 * and 
	 * @param user
	 * @param amountAsked
	 * @param description
	 * @return
	 */

	public Map<MoneyTransfer, Double> processTransferFromBank(
			User user, Double amountAsked, String description) {
		
		Map<MoneyTransfer, Double> map = new HashMap<MoneyTransfer, Double>();
		MoneyTransfer fromBankToUser = new MoneyTransfer();
		Double moneyCollected = 0.00;
		
		fromBankToUser.setMoneySender(user);
		fromBankToUser.setDescription(description);
		fromBankToUser.setDate(OffsetDateTime.now());
		TypeOfTransfer type = typeService.getById(3);
		fromBankToUser.setTypeOfTransfer(type);

		Double amountResult = bankService.fundAppAccount(user, amountAsked);
		if(amountResult == 0.00) {
			LOGGER.info("no money will be added on appAccount");
		}else {		
			Double amount = typeService.amountFromTypeOfTransfer(amountResult, type.getId());
			moneyCollected = typeService.moneyCollected(amountResult, type.getId());
			Double amountTotal = user.getAppAccount() + amount;	
			
			try {
				if(amountTotal <= amountMax) {
					fromBankToUser.setAmount(amountAsked);
					user.setAppAccount(amountTotal);
					List<MoneyTransfer> list = user.getMoneyTransfers();
					if(list == null) {
						list = new ArrayList<>();
					}
					list.add(fromBankToUser);
					user.setMoneyTransfers(list);
					
				}else {
					LOGGER.error("quota reached, the amountMax="+amountMax+" will be reached.");
					throw new QuotaReachedException("quota reached exception.");				
				}
			} catch (QuotaReachedException e) {
				e.printStackTrace();
			}
			
			//Saving in database the transfer
			try {
				moneyTransferRepository.save(fromBankToUser);
				userService.save(user);
			} catch(Exception ex) {
				ex.printStackTrace();
			}
		}
		map.put(fromBankToUser, moneyCollected);
		return map;
	}
	
	public Map<MoneyTransfer, Double> processTransferToBank(
			User user, Double amountTransfered, String description) {
		Map<MoneyTransfer, Double> map = new HashMap<MoneyTransfer, Double>();
		MoneyTransfer fromUserToBank = new MoneyTransfer();
		Double moneyCollected = 0.00;

		fromUserToBank.setMoneySender(user);
		fromUserToBank.setDescription(description);
		fromUserToBank.setDate(OffsetDateTime.now());
		TypeOfTransfer type = typeService.getById(2);
		fromUserToBank.setTypeOfTransfer(type);

		
		try {
			Double amountUserOnApp = user.getAppAccount();
			if(amountUserOnApp >= amountTransfered) {
				Double amountToSend = typeService.amountFromTypeOfTransfer(amountTransfered, type.getId());
				moneyCollected = typeService.moneyCollected(amountTransfered, type.getId());
				bankService.fundBankAccount(user, amountToSend);
				Double amountResult = amountUserOnApp - amountTransfered;
				user.setAppAccount(amountResult);
				LOGGER.info("user appAccount is now = " + amountResult+".");
				fromUserToBank.setAmount(amountTransfered);
			}else {
				LOGGER.error("Trying to transfer more money than is present on app account.");
				throw new NotEnoughMoneyException("not enough money on app account.");
			}
		} catch (NotEnoughMoneyException e) {
			e.printStackTrace();
		}
				
		//Saving in database the transfer
		try {
			moneyTransferRepository.save(fromUserToBank);
			userService.save(user);
			
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
		map.put(fromUserToBank, moneyCollected);
		return map;
	}
	
	public Map<MoneyTransfer, Double> processTransferToFriend(
			User user,User moneyFriend, Double amountAsked, String description) {
	return null;
	}
}
