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
import com.PMB.PayMyBuddy.Service.UserService;
import com.PMB.PayMyBuddy.model.BankAccount;
import com.PMB.PayMyBuddy.model.User;
import com.PMB.PayMyBuddy.repository.BankAccountRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PayMyBuddyApplication.class)
public class BankAccountServiceTest {

	@Autowired
	BankAccountService bankAccountService;

	@MockBean
	BankAccountRepository bankRepository;

	@MockBean
	UserService userService;

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
		userService.save(user);

		Mockito.when(userService.getUserByEmail(user.getEmail())).thenReturn(user);
	}

	@Test
	public void whenUser_wantsMoneyFromBankaccount_thenMoneyIsReturned() {
		// Given
		User user = userService.getUserByEmail("test@test.com");
		BankAccount bankAccount = user.getBankAccount();
		bankAccount.setResponseFromBankApi(true);
		Double amountAsked = 100.00;
		// When
		Double bankResponse = bankAccountService.fundAppAccount(user, amountAsked);
		// Then
		assertEquals(bankResponse, 100.00);
	}

	@Test
	public void when_noMoneyOnBankaccount_thenNoMoneyIsReturned() {
		// Given
		User user = userService.getUserByEmail("test@test.com");
		BankAccount bankAccount = user.getBankAccount();
		bankAccount.setResponseFromBankApi(false);
		Double amountAsked = 100.00;
		// When
		bankAccountService.fundAppAccount(user, amountAsked);
		// Then
		assertEquals(user.getAppAccount(), 0);
	}

	@Test
	public void whenUser_tryToTransferMoreMoneyThanOnHisAppAccount_thenNoMoneyIsTransfered() {
		// Given
		User user = userService.getUserByEmail("test@test.com");
		user.setAppAccount(100.00);
		Double amountGiven = 101.00;
		// When
		bankAccountService.fundBankAccount(user, amountGiven);
		// Then
		assertEquals(user.getAppAccount(), 100.00);
	}

	@Test
	public void whenUser_addBankAccountAndHaveOneAlready_thenBankAccountNotSaved() {
		// Given
		User user = userService.getUserByEmail("test@test.com");
		BankAccount bankAccount = new BankAccount("name of the bank", "123456789");
		// When
		bankAccountService.saveBankAccount(user, bankAccount);
		// Then
		assertEquals("banque de Test", user.getBankAccount().getBankName());
	}
	
	@Test
	public void whenUser_addBankAccountAndDoesNotHaveOne_thenBankAccountSaved() {
		// Given
		User userNew = new User("new@test.com", "1234", "test", "test", null, 0, "ROLE_USER", true, null, null, null,
				null, null);
		Mockito.when(userService.getUserByEmail("new@test.com")).thenReturn(userNew);		
		BankAccount bankAccount = new BankAccount("name of the bank", "123456789");
		// When
		bankAccountService.saveBankAccount(userNew, bankAccount);
		// Then
		assertEquals("name of the bank", userNew.getBankAccount().getBankName());
	}
	
}
