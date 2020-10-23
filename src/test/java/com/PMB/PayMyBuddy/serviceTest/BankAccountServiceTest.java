//package com.PMB.PayMyBuddy.serviceTest;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//
//import com.PMB.PayMyBuddy.Service.BankAccountService;
//import com.PMB.PayMyBuddy.model.BankAccount;
//import com.PMB.PayMyBuddy.model.User;
//import com.PMB.PayMyBuddy.repository.UserRepository;
//import com.PMB.PayMyBuddy.repository.bankAccountRepository;
//
//public class BankAccountServiceTest {
//
//	@Autowired
//	BankAccountService bankAccountService;
//	
//	@MockBean
//	bankAccountRepository bankRepository;
//	
//	@MockBean
//	UserRepository userRepository;
//	
//	
//	@BeforeEach
//    public void setUp() {
//		User user = new User();
//        user.setEmail("test@test.com");
//        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
//        String password = bCryptPasswordEncoder.encode("1234");
//        user.setPassword(password);
//        user.setFirstname("test");
//        user.setEnabled(true);
//        user.setRole("ROLE_USER");
//        userRepository.save(user);
//        
//			
//        Mockito.when(userRepository.findByEmail(user.getEmail()))
//        	.thenReturn(user);
//	}
//	
//	@Test
//	public void getOkIfMoneyCanBeTransferredFromBankAccountToAppAccount() {
//		//Given
//		User user = userRepository.findByEmail("test@test.com");
//		//When
//		String moneyIsTransferred = bankAccountService.fundAppAccount(user);
//		
//		//Then
//		assertEquals(moneyIsTransferred, "no money on this account, tranfer refused");
//	}
//}
