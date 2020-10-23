package com.PMB.PayMyBuddy.serviceTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
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
import com.PMB.PayMyBuddy.Service.UserService;
import com.PMB.PayMyBuddy.model.User;
import com.PMB.PayMyBuddy.repository.UserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PayMyBuddyApplication.class)
public class UserServiceTest {


	@Autowired
    private UserService userService;
 
    @MockBean
    private UserRepository userRepository;
	
    
	@BeforeEach
    public void setUp() {
		User user = new User();
        user.setEmail("test@test.com");
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String password = bCryptPasswordEncoder.encode("1234");
        user.setPassword(password);
        user.setFirstname("test");
        user.setEnabled(true);
        user.setRole("ROLE_USER");
        userRepository.save(user);
        
			
        Mockito.when(userRepository.findByEmail(user.getEmail()))
        	.thenReturn(user);
        
       
      
	}

	
	@Test
	public void whenValidEmail_thenUSerShouldBeFound() {
    String email ="test@test.com";
    User found = userService.getUserByEmail(email);
 
     assertThat(found.getFirstname()).isEqualTo("test");
	}

	@Test
	public void testGeListUsersReturnsFindAllUsersFromRepository() {
		
		//String email, String password, String firstname, String lastname, Date birthdate,
		//double appAccount, String role, boolean enabled
	    
		User user = new User(0, "test@test.com", "1234", "test", "test", null, 0, "ROLE_USER",true, null, null, null, null, null);      
        User user2 = new User(0, "x@x.com", "1234", "x", "x", null, 0, "ROLE_ADMIN", true, null, null, null, null, null);
        List<User> users = java.util.Arrays.asList(user, user2);
		System.out.println(users);
		when(userRepository.findAll()).thenReturn(users);
		
		//Act
		List<User> result = userService.findAll();
		System.out.println("result "+result);
		//Assert
		assertTrue(result.size() == 2);
		assertTrue(result.get(0).getFirstname().equals("test"));
		assertTrue(result.get(1).getFirstname().equals("x"));
	}
}
