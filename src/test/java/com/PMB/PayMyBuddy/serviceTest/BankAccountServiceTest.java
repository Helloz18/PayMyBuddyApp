package com.PMB.PayMyBuddy.serviceTest;


import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import com.PMB.PayMyBuddy.PayMyBuddyApplication;
import com.PMB.PayMyBuddy.Service.BankAccountService;
import com.PMB.PayMyBuddy.model.BankAccount;
import com.PMB.PayMyBuddy.model.User;
import com.PMB.PayMyBuddy.repository.UserRepository;
import com.PMB.PayMyBuddy.repository.bankAccountRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PayMyBuddyApplication.class)
public class BankAccountServiceTest {

	@Autowired
	BankAccountService bankAccountService;
	
	@MockBean
	bankAccountRepository bankRepository;
	
	@MockBean
	UserRepository userRepository;
	
	
	@BeforeEach
    public void setUp() {

		BankAccount bankAccount = new BankAccount("banque de Test", "123456789");
		
		User user = new User();
        user.setEmail("test@test.com");
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String password = bCryptPasswordEncoder.encode("1234");
        user.setPassword(password);
        user.setFirstname("test");
        user.setLastname("lastnameTest"); 
        user.setEnabled(true);
        user.setRole("ROLE_USER");
        user.setAppAccount(0);
        user.setBankAccount(bankAccount);
        userRepository.save(user);
        
			
        Mockito.when(userRepository.findByEmail(user.getEmail()))
        	.thenReturn(user);
	}
	
	
//Tests Bankaccount to AppAccount	
	@Test
	public void whenUserWantsMoneyFromBankaccountThenMoneyTransferedOnAppAccount() {
		//Given
		User user = userRepository.findByEmail("test@test.com");
		BankAccount bankAccount = user.getBankAccount();
		bankAccount.setAccountHasMoney(true);
		Double amountAsked = 100.00;
				
		//When
		bankAccountService.fundAppAccount(user,amountAsked);
		
		//Then
		assertEquals(user.getAppAccount(), 100.00);
		
	}
	
	@Test
	public void whenNoMoneyOnBankaccountThenNoMoneyTransferedOnAppAccount() {
		//Given
		User user = userRepository.findByEmail("test@test.com");
		BankAccount bankAccount = user.getBankAccount();
		bankAccount.setAccountHasMoney(false);
		Double amountAsked = 100.00;
						
		//When
		bankAccountService.fundAppAccount(user,amountAsked);
				
		//Then
		assertEquals(user.getAppAccount(), 0);
				
	}
	
	@Test
	public void whenAppAccountIsFullThenNoMoneyTransferedOnAppAccount() {
		//Given
		User user = userRepository.findByEmail("test@test.com");
		BankAccount bankAccount = user.getBankAccount();
		bankAccount.setAccountHasMoney(true);
		Double amountAsked = 10000.00;
								
		//When
		bankAccountService.fundAppAccount(user,amountAsked);
						
		//Then
		assertEquals(user.getAppAccount(), 0);
	}
	
	
//Tests AppAccount to BankAccount
	@Test
	public void whenUserTransferedMoneyFromAppAccountToBankaccountThenMoneyIsTransfered() {
		//Given
		User user = userRepository.findByEmail("test@test.com");
		user.setAppAccount(200.00);
		Double amountGiven = 100.00;		
		
		//When
		bankAccountService.fundBankAccount(user, amountGiven);
		
		//Then
		assertEquals(user.getAppAccount(), (200.00 - 100.00));
	}
	
	
	@Test
	public void whenUserTryToTransferMoreMoneyThanOnHisAppAccountThenNoMoneyIsTransfered() {
		//Given
		User user = userRepository.findByEmail("test@test.com");
		user.setAppAccount(100.00);
		Double amountGiven = 101.00;
		
		//When
		bankAccountService.fundBankAccount(user, amountGiven);
		
		//Then
		assertEquals(user.getAppAccount(), 100.00);
		
	}

}
