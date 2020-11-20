package com.PMB.PayMyBuddy.controllerTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.PMB.PayMyBuddy.Service.BankAccountService;
import com.PMB.PayMyBuddy.Service.MoneyTransferService;
import com.PMB.PayMyBuddy.Service.UserService;
import com.PMB.PayMyBuddy.controller.UserController;
import com.PMB.PayMyBuddy.model.BankAccount;
import com.PMB.PayMyBuddy.model.MoneyTransfer;
import com.PMB.PayMyBuddy.model.TypeOfTransfer;
import com.PMB.PayMyBuddy.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = UserController.class)
@WithMockUser(roles = { "USER", "ADMIN" })
public class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	UserService userService;
	
	@MockBean
	MoneyTransferService moneyTransferService;
	
	@MockBean
	BankAccountService bankService;
	
	@MockBean
	DataSource dataSource;

	@BeforeEach
	public void setUp() {
		
	}

	
	@Test
	public void getUsersShouldReturnOk() throws Exception {
		mockMvc.perform(get("/admin/all")).andExpect(status().isOk());
	}
	
	@Test
	public void getUserShouldReturnOk() throws Exception {
		mockMvc.perform(get("/user/")).andExpect(status().isOk());
	}
	
	@Test
	public void getUserMoneyTransfersShouldReturnOk() throws Exception {
		User user = new User("test@test.com", "123", "test", "test", null, 0, "ROLE_USER", true, null, null, null,null, null);
		User user2 = new User("new@test.com", "123", "test", "test", null, 0, "ROLE_USER", true, null, null, null,null, null);
	
		List<User> moneyFriends = new ArrayList<>();
		moneyFriends.add(user);
		moneyFriends.add(user2);
		user.setMoneyFriends(moneyFriends);
		System.out.println(user);
		TypeOfTransfer type = new TypeOfTransfer("",50.00);
		MoneyTransfer money = new MoneyTransfer(100.00, "test", OffsetDateTime.now(),type,user,user2);	
		System.out.println(money);	
		List<MoneyTransfer> list = new ArrayList<>();
		list.add(money);
		user.setMoneyTransfers(list);
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		
		
		given(userService.getUserByEmail(userDetails.getUsername())).willReturn(user);
		System.out.println("user "+user);
		mockMvc.perform(get("/user/myMoneyTransfers")
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());
	}

	@Test
	public void givenUsers_whenGetUsers_thenReturnJsonArray() throws Exception {

		User user = new User("test@test.com", "123", "test", "test", null, 0, "ROLE_USER", true, null, null, null, null, null);
		User user2 = new User("x@x.com", "123", "x", "x", null, 0, "ROLE_ADMIN", true, null, null, null, null, null);
		List<User> users = new ArrayList<>();
		users.add(user);
		users.add(user2);
		
		given(userService.findAll()).willReturn(users);
			
		mockMvc.perform(get("/admin/all")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[0].email", is(user.getEmail())));

	}
	
	@Test
	public void givenUser_whenAddNewFriend_thenReturnJsonArray() throws Exception {
		User user = new User("test@test.com", "123", "test", "test", null, 0, "ROLE_USER", true, null, null, null, null, null);
		User user2 = new User("x@x.com", "123", "x", "x", null, 0, "ROLE_ADMIN", true, null, null, null, null, null);
		user.setId((long) 1);
		given(userService.get((long) 1)).willReturn(user);
		
		mockMvc.perform(post("/user/moneyFriends")
				.param("moneyFriendEmail", user2.getEmail())
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}
	
	@Test
	public void givenUser_whenDeleteFriend_thenReturnJsonArray() throws Exception {
		User user = new User("test@test.com", "123", "test", "test", null, 0, "ROLE_USER", true, null, null, null, null, null);
		User user2 = new User("x@x.com", "123", "x", "x", null, 0, "ROLE_ADMIN", true, null, null, null, null, null);
		user.setId((long) 1);
		List<User> moneyFriends = new ArrayList<>();
		moneyFriends.add(user2);
		user.setMoneyFriends(moneyFriends);
		given(userService.get((long) 1)).willReturn(user);
		
		mockMvc.perform(delete("/user/moneyFriends")
				.param("moneyFriendEmail", user2.getEmail())
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}
	
	@Test
	public void givenUser_whenAddMoneyFromBank_thenReturnJsonArray() throws Exception {
		User user = new User("test@test.com", "123", "test", "test", null, 0, "ROLE_USER", true, null, null, null, null, null);
		user.setId((long) 1);
		given(userService.get((long) 1)).willReturn(user);
		Double amountAsked = 100.00;
		String description = "take 100.00 from my bank";
		
		mockMvc.perform(post("/user/moneyTransfer")
				.param("amountAsked", amountAsked.toString())
				.param("description", description)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}
	
	@Test
	public void givenUser_whenAddBankAccount_thenReturnJsonArray() throws Exception {
		User user = new User("test@test.com", "123", "test", "test", null, 0, "ROLE_USER", true, null, null, null, null, null);
		user.setId((long) 1);
		given(userService.get((long) 1)).willReturn(user);
		BankAccount bankAccount= new BankAccount("my bank", "123456789");
		bankAccount.setResponseFromBankApi(true);
		System.out.println(bankAccount);
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(bankAccount);
		
		mockMvc.perform(post("/user/bankAccount")
				.content(json)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}
}
