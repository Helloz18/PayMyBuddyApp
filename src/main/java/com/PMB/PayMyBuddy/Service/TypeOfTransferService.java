package com.PMB.PayMyBuddy.Service;

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
	
	public Double amountFromTypeOfTransfer(Double amount, int id) {
		TypeOfTransfer type = typeRepository.getOne(id);
		Double amountProcessed = amount - (amount * type.getPercentToCollect()/100);
		return amountProcessed;		
	}
	
	public Double moneyCollected(Double amount, int id) {
		TypeOfTransfer type = typeRepository.getOne(id);
		Double moneyCollected = amount * type.getPercentToCollect()/100;
		return moneyCollected;		
	}
	
	public TypeOfTransfer getById(int id) {
		return typeRepository.getOne(id);
	}
	
}
