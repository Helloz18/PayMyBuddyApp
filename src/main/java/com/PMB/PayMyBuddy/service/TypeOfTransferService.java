package com.PMB.PayMyBuddy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.PMB.PayMyBuddy.model.TypeOfTransfer;
import com.PMB.PayMyBuddy.repository.TypeOfTransferRepository;

@Service
@Transactional
public class TypeOfTransferService {

	@Autowired
	TypeOfTransferRepository typeRepository;
	
	/**
	 * This method will get the amount received by a friend :
	 * amount send by the user minus the amount collected to fund the application.
	 * @param amount
	 * @param id : the id of the type of transfer
	 * @return
	 */
	public Double amountFromTypeOfTransfer(Double amount, int id) {
		TypeOfTransfer type = typeRepository.getOne(id);
		Double amountProcessed = amount - (amount * type.getPercentToCollect()/100);
		return amountProcessed;		
	}
	
	/**
	 * This method will return the amount collected to fund the application.
	 * @param amount
	 * @param id
	 * @return
	 */
	public Double moneyCollected(Double amount, int id) {
		TypeOfTransfer type = typeRepository.getOne(id);
		Double moneyCollected = amount * type.getPercentToCollect()/100;
		return moneyCollected;		
	}
	
	/**
	 * This method will ask the data access layer to return the type of transfer by its Id.
	 * @param id
	 * @return
	 */
	public TypeOfTransfer getById(int id) {
		return typeRepository.getOne(id);
	}
	
}
