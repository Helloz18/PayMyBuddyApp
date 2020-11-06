package com.PMB.PayMyBuddy.serviceTest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import com.PMB.PayMyBuddy.PayMyBuddyApplication;
import com.PMB.PayMyBuddy.Service.BankAccountService;
import com.PMB.PayMyBuddy.Service.MoneyTransferService;
import com.PMB.PayMyBuddy.Service.TypeOfTransferService;
import com.PMB.PayMyBuddy.Service.UserService;
import com.PMB.PayMyBuddy.exception.QuotaReachedException;
import com.PMB.PayMyBuddy.model.BankAccount;
import com.PMB.PayMyBuddy.model.MoneyTransfer;
import com.PMB.PayMyBuddy.model.TypeOfTransfer;
import com.PMB.PayMyBuddy.model.User;
import com.PMB.PayMyBuddy.repository.MoneyTransferRepository;
import com.PMB.PayMyBuddy.repository.TypeOfTransferRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PayMyBuddyApplication.class)
class MoneyTransferServiceTest {
		
	@Autowired
	MoneyTransferService moneyTransferService;
	
	@MockBean
	MoneyTransferRepository moneyTransferRepository;
	
	@MockBean
	BankAccountService bankService;
	
	@MockBean
	UserService userService;
	
	@MockBean
	TypeOfTransferService typeService;
	
	
	@BeforeEach
    public void setUp() {

		User user = new User("test@test.com", "123", "test", "test", null, 0, "ROLE_USER", true, null, null, null, null, null);
		User user2 = new User("x@x.com", "123", "x", "x", null, 0, "ROLE_ADMIN", true, null, null, null, null, null);
		 Mockito.when(userService.getUserByEmail(user2.getEmail()))
     	.thenReturn(user2);      
 
        List<User> moneyFriends = new ArrayList<>();
        moneyFriends.add(user);
        moneyFriends.add(user2);
        user.setMoneyFriends(moneyFriends);
                
        BankAccount bankAccount = new BankAccount("banque de Test", "123456789");
		bankAccount.setResponseFromBankApi(true);
		user.setBankAccount(bankAccount);
		
        Mockito.when(userService.getUserByEmail(user.getEmail()))
        	.thenReturn(user);      
    }

	@Test
	public void whenUserSendsMoneyToMoneyFriendThenAppAccountIsCutFromAmount() {
	}
	
	@Test
	public void whenUserSendsMoneyToMoneyFriendThenMoneyFriendAppAccountIsCredited() {
	}

	@Test
	public void whenUserSendsMoneyToFriendThenApercentIsCollected() {
		
	}
	
	@Test
	public void 
	whenUserGetsMoneyFromHisBankAndAmountMaxNotReachedThenAmountIsSendOnAppAccount() {
		//GIVEN
		User user = userService.getUserByEmail("test@test.com");
		Double amountAsked = 100.00;
		Mockito.when(bankService.fundAppAccount(user, amountAsked))
     		.thenReturn(amountAsked);
		String description = "je prends "+amountAsked+" sur mon compte en banque.";
		TypeOfTransfer type = new TypeOfTransfer("text", 10.00);
		type.setId(3);
	    Mockito.when(typeService.getById(3)).thenReturn(type);
	    Mockito.when(typeService.amountFromTypeOfTransfer(100.00, type.getId()))
	        .thenReturn(90.00);
	    MoneyTransfer key = new MoneyTransfer();
			
	    //WHEN    
		Map<MoneyTransfer, Double> map = moneyTransferService.
				processTransferFromBank(user, amountAsked, description);
		for(Map.Entry<MoneyTransfer, Double> entry : map.entrySet()) {
			key = entry.getKey();
		}
		//THEN
		assertEquals(100.00, key.getAmount());
		assertEquals(90.00, key.getMoneySender().getAppAccount());
		assertEquals(null, key.getMoneyFriend());
		
	}
	
	@Test
	void whenUserReachedAmountMaxThenNoTransferIsDone() {
		//GIVEN
		User user = userService.getUserByEmail("test@test.com");
		Double amountAsked = 10000.00;
		Mockito.when(bankService.fundAppAccount(user, amountAsked))
     		.thenReturn(amountAsked);
		String description = "je prends "+amountAsked+" sur mon compte en banque.";
		TypeOfTransfer type = new TypeOfTransfer("text", 0.00);
		type.setId(3);
	    Mockito.when(typeService.getById(3)).thenReturn(type);
	    Mockito.when(typeService.amountFromTypeOfTransfer(10000.00, type.getId()))
	        .thenReturn(10000.00);
	    
		MoneyTransfer key = new MoneyTransfer();
			
	    //WHEN    
		Map<MoneyTransfer, Double> map = moneyTransferService.
				processTransferFromBank(user, amountAsked, description);
		for(Map.Entry<MoneyTransfer, Double> entry : map.entrySet()) {
			key = entry.getKey();
		}
		//THEN
		assertEquals(0, key.getAmount());
		
	}
	
	@Test
	void whenUserFundHisBankAccountThenAppAccountIsCut() {
		//GIVEN
		User user = userService.getUserByEmail("test@test.com");
		user.setAppAccount(200.00);
		Double amountTransfered = 100.00;
		String description = "je prends "+amountTransfered+" sur mon app account.";
			
		TypeOfTransfer type = new TypeOfTransfer("text", 10.00);
		type.setId(2);
	    Mockito.when(typeService.getById(2)).thenReturn(type);
	    Mockito.when(typeService.amountFromTypeOfTransfer(100.00, type.getId()))
	        .thenReturn(90.00);
	    
		MoneyTransfer key = new MoneyTransfer();
		
		//WHEN    
		Map<MoneyTransfer, Double> map = moneyTransferService.
				processTransferToBank(user, amountTransfered, description);
		for(Map.Entry<MoneyTransfer, Double> entry : map.entrySet()) {
			key = entry.getKey();
		}
		//THEN
		assertEquals(100.00, key.getAmount());
		assertEquals(100.00, key.getMoneySender().getAppAccount());
				
     
	}

}
