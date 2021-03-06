package com.PMB.PayMyBuddy.serviceTest;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.PMB.PayMyBuddy.PayMyBuddyApplication;
import com.PMB.PayMyBuddy.model.TypeOfTransfer;
import com.PMB.PayMyBuddy.repository.TypeOfTransferRepository;
import com.PMB.PayMyBuddy.service.TypeOfTransferService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PayMyBuddyApplication.class)
class TypeOfTransferServiceTest {

	@Autowired
	TypeOfTransferService typeService;
	
	@MockBean
	TypeOfTransferRepository typeRepository;
	
	
	@BeforeEach
    public void setUp() {

		 TypeOfTransfer type = new TypeOfTransfer("test", 10.00);
	        type.setId(3);
	        Mockito.when(typeRepository.getOne(3))
	    	.thenReturn(type);
		}


	@Test
	void when_getByNameIsCalled_thenTypeOfTransferIsReturned() {
		//GIVEN
		int id = 3;
		//WHEN
		TypeOfTransfer type = typeService.getById(id);
		//THEN
		assertEquals(3, type.getId());
	}
	
	@Test
	void when_amountIsTransfered_thenPercentIsCollected() {
		//GIVEN
		int id = typeRepository.getOne(3).getId();
		Double amount = 100.0;
		//WHEN
		Double response = typeService.amountFromTypeOfTransfer(amount, id);
		//THEN
		assertEquals(90.00, response);
		
	}

	@Test
	void when_percentIsNull_thenNoMoneyIsRemoved() {
		//GIVEN
		TypeOfTransfer type = typeRepository.getOne(3);
		type.setPercentToCollect(0.00);
		Double amount = 100.0;
		//WHEN
		Double response = typeService.amountFromTypeOfTransfer(amount, type.getId());
		//THEN
		assertEquals(100.0, response);
	}
	
	@Test
	void when_percentNotNull_thenMoneyIsCollected() {
		//GIVEN
		int id = typeRepository.getOne(3).getId();
		Double amount = 100.0;
		//WHEN
		Double response = typeService.moneyCollected(amount, id);
		//THEN
		assertEquals(10.00, response);			
	}
	
	@Test
	void when_percentIsNull_thenNoMoneyIsCollected() {
		//GIVEN
		TypeOfTransfer type = typeRepository.getOne(3);
		type.setPercentToCollect(0.00);
		Double amount = 100.0;
		//WHEN
		Double response = typeService.moneyCollected(amount, type.getId());
		//THEN
		assertEquals(0.0, response);
	}
	
		
}
