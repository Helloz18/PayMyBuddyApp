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
import com.PMB.PayMyBuddy.repository.TypeOfTransferRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PayMyBuddyApplication.class)
class MoneyTransferServiceTest {
	
	@Autowired
	MoneyTransferService moneyTransferService;
	
	@MockBean
	BankAccountService bankService;
	
	@MockBean
	UserService userService;
	
	@MockBean
	TypeOfTransferService typeService;
	
	
	@BeforeEach
    public void setUp() {

		
		User user = new User();
		user.setId(1);
        user.setEmail("test@test.com");
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String password = bCryptPasswordEncoder.encode("1234");
        user.setPassword(password);
        user.setFirstname("test");
        user.setLastname("lastnameTest"); 
        user.setEnabled(true);
        user.setRole("ROLE_USER");
        user.setAppAccount(0.00);
        
        BankAccount bankAccount2 = new BankAccount("banque de user2", "987654321");
        bankAccount2.setId(2);
        bankService.save(bankAccount2);
        User user2 = new User();
        user.setId(2);
        user2.setEmail("user2@test.com");
        BCryptPasswordEncoder bCryptPasswordEncoder2 = new BCryptPasswordEncoder();
        String password2 = bCryptPasswordEncoder2.encode("1234");
        user2.setPassword(password2);
        user2.setFirstname("user2");
        user2.setLastname("lastnameTest2"); 
        user2.setEnabled(true);
        user2.setRole("ROLE_USER");
        user2.setAppAccount(100.00);
        user2.setBankAccount(bankAccount2);
        userService.save(user2);
       
        List<User> moneyFriends = new ArrayList<>();
        moneyFriends.add(user);
        moneyFriends.add(user2);
        user.setMoneyFriends(moneyFriends);
                
        userService.save(user);
        
        BankAccount bankAccount = new BankAccount("banque de Test", "123456789");
        bankAccount.setId(1);
		bankAccount.setResponseFromBankApi(true);
		bankService.save(bankAccount);
		user.setBankAccount(bankAccount);
        	System.out.println(user);	
        Mockito.when(userService.getUserByEmail(user.getEmail()))
        	.thenReturn(user);
        
        TypeOfTransfer type = new TypeOfTransfer("text", 10.00);
        typeService.save(type);
        
        Mockito.when(typeService.getById(3))
    	.thenReturn(type);
        
       
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
	public void whenUserGetsMoneyFromHisBankThenPercentToCollectIsSetTo3() {
		User user = userService.getUserByEmail("test@test.com");
		Double amountAsked = 100.00;
		 Mockito.when(bankService.fundAppAccount(user, amountAsked))
     	.thenReturn(amountAsked);
 
		String description = "je prends "+amountAsked+" sur mon compte en banque.";
		MoneyTransfer key = new MoneyTransfer();
		
		Map<MoneyTransfer, Double> map = moneyTransferService.processTransferFromBank(user, amountAsked, description);
		for(Map.Entry<MoneyTransfer, Double> entry : map.entrySet()) {
			key = entry.getKey();
		}
		System.out.println("key" +key);
		assertEquals(key.getTypeOfTransfer().getId(), 3);
		
	}
	
}
