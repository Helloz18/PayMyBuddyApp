package com.PMB.PayMyBuddy.Service;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
		//For this type of transfer the receiver is the sender
		fromBankToUser.setMoneyFriend(user);
		fromBankToUser.setDescription(description);
		fromBankToUser.setDate(OffsetDateTime.now());
		TypeOfTransfer type = typeService.getById(3);
		fromBankToUser.setTypeOfTransfer(type);
		Double amountResult = bankService.fundAppAccount(user, amountAsked);
		System.out.println("amount asked "+ amountAsked + "amountAsked from moneytransfer "+ amountResult);
		if(amountResult == 0.00) {
			LOGGER.info("no money will be added on appAccount");
		}else {			
			Double amount = typeService.amountFromTypeOfTransfer(amountResult, type.getId());
			moneyCollected = typeService.moneyCollected(amountResult, type.getId());
			Double amountTotal = user.getAppAccount() + amount;		
			
			try {
				if(amountTotal <= amountMax) {
					fromBankToUser.setAmount(amountTotal);
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
			} catch(Exception ex) {
				ex.printStackTrace();
			}
		}
		map.put(fromBankToUser, moneyCollected);
		return map;
	}
	
}
