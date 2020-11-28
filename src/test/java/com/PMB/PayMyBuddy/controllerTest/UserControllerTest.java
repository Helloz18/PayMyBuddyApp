package com.PMB.PayMyBuddy.controllerTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.PMB.PayMyBuddy.Service.BankAccountService;
import com.PMB.PayMyBuddy.Service.MoneyTransferService;
import com.PMB.PayMyBuddy.Service.UserService;
import com.PMB.PayMyBuddy.controller.UserController;
import com.PMB.PayMyBuddy.model.Address;
import com.PMB.PayMyBuddy.model.BankAccount;
import com.PMB.PayMyBuddy.model.MoneyTransfer;
import com.PMB.PayMyBuddy.model.PhoneNumber;
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
	public void getUsers_ShouldReturnOk() throws Exception {
		mockMvc.perform(get("/admin/all")).andExpect(status().isOk());
	}

	@Test
	public void getUser_ShouldReturnOk() throws Exception {
		mockMvc.perform(get("/user/")).andExpect(status().isOk());
	}

	@Test
	public void getUserMoneyTransfers_ShouldReturnOk() throws Exception {
		// GIVEN, WHEN
		User user = new User("test@test.com", "123", "test", "test", null, 0, "ROLE_USER", true, null, null, null, null,
				null);
		User user2 = new User("new@test.com", "123", "test", "test", null, 0, "ROLE_USER", true, null, null, null, null,
				null);
		List<User> moneyFriends = new ArrayList<>();
		moneyFriends.add(user);
		moneyFriends.add(user2);
		user.setMoneyFriends(moneyFriends);
		TypeOfTransfer type = new TypeOfTransfer("type of transfer", 50.00);
		MoneyTransfer money = new MoneyTransfer(100.00, "test", OffsetDateTime.now(), type, user, user2);
		List<MoneyTransfer> list = new ArrayList<>();
		list.add(money);
		user.setMoneyTransfers(list);
		given(userService.getConnectedUser()).willReturn(user);
		// THEN
		mockMvc.perform(get("/user/myMoneyTransfers").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

	@Test
	public void givenUsers_whenGetUsers_thenReturnJsonArray() throws Exception {
		// GIVEN, WHEN
		User user = new User("test@test.com", "123", "test", "test", null, 0, "ROLE_USER", true, null, null, null, null,
				null);
		User user2 = new User("x@x.com", "123", "x", "x", null, 0, "ROLE_ADMIN", true, null, null, null, null, null);
		List<User> users = new ArrayList<>();
		users.add(user);
		users.add(user2);
		given(userService.findAll()).willReturn(users);
		// THEN
		mockMvc.perform(get("/admin/all").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(2))).andExpect(jsonPath("$[0].email", is(user.getEmail())));

	}

	@Test
	public void givenUser_whenAddNewFriend_thenReturnOk() throws Exception {
		// GIVEN,WHEN
		User user = new User("test@test.com", "123", "test", "test", null, 0, "ROLE_USER", true, null, null, null, null,
				null);
		User user2 = new User("x@x.com", "123", "x", "x", null, 0, "ROLE_ADMIN", true, null, null, null, null, null);
		user.setId((long) 1);
		given(userService.get((long) 1)).willReturn(user);
		// THEN
		mockMvc.perform(post("/user/moneyFriends").param("moneyFriendEmail", user2.getEmail())
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	}

	@Test
	public void givenUser_whenDeleteFriend_thenReturnOk() throws Exception {
		// GIVEN, WHEN
		User user = new User("test@test.com", "123", "test", "test", null, 0, "ROLE_USER", true, null, null, null, null,
				null);
		User user2 = new User("x@x.com", "123", "x", "x", null, 0, "ROLE_ADMIN", true, null, null, null, null, null);
		user.setId((long) 1);
		List<User> moneyFriends = new ArrayList<>();
		moneyFriends.add(user2);
		user.setMoneyFriends(moneyFriends);
		given(userService.get((long) 1)).willReturn(user);
		// THEN
		mockMvc.perform(delete("/user/moneyFriends").param("moneyFriendEmail", user2.getEmail())
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	}

	@Test
	public void givenUser_whenAddMoneyFromBank_thenReturnOk() throws Exception {
		// GIVEN, WHEN
		User user = new User("test@test.com", "123", "test", "test", null, 0, "ROLE_USER", true, null, null, null, null,
				null);
		user.setId((long) 1);
		given(userService.get((long) 1)).willReturn(user);
		Double amountAsked = 100.00;
		String description = "take 100.00 from my bank";
		// THEN
		mockMvc.perform(post("/user/moneyTransfer").param("amountAsked", amountAsked.toString())
				.param("description", description).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	}

	@Test
	public void givenUser_whenAddBankAccount_thenReturnOk() throws Exception {
		// GIVEN, WHEN
		User user = new User("test@test.com", "123", "test", "test", null, 0, "ROLE_USER", true, null, null, null, null,
				null);
		user.setId((long) 1);
		given(userService.get((long) 1)).willReturn(user);
		BankAccount bankAccount = new BankAccount("my bank", "123456789");
		bankAccount.setResponseFromBankApi(true);
		System.out.println(bankAccount);
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(bankAccount);
		// THEN
		mockMvc.perform(post("/user/bankAccount").content(json).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

	@Test
	public void givenUser_whenAddAddress_thenReturnOk() throws Exception {
		// GIVEN, WHEN
		User user = new User("test@test.com", "123", "test", "test", null, 0, "ROLE_USER", true, null, null, null, null,
				null);
		given(userService.getConnectedUser()).willReturn(user);

		Address address = new Address("1", "stree", "zip123", "city");
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(address);
		// THEN
		mockMvc.perform(post("/user/address").content(json).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

	@Test
	public void givenUser_whenAddPhone_thenReturnOk() throws Exception {
		// GIVEN, WHEN
		User user = new User("test@test.com", "123", "test", "test", null, 0, "ROLE_USER", true, null, null, null, null,
				null);
		given(userService.getConnectedUser()).willReturn(user);

		PhoneNumber phone = new PhoneNumber("home", "0123456789");
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(phone);
		// THEN
		mockMvc.perform(post("/user/phone").content(json).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

	@Test
	public void givenUser_whenDeactivate_thenReturnOk() throws Exception {
		// GIVEN, WHEN
		User user = new User("test@test.com", "123", "test", "test", null, 0, "ROLE_USER", true, null, null, null, null,
				null);
		given(userService.getConnectedUser()).willReturn(user);
		// THEN
		mockMvc.perform(put("/user/deactivate").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

	}

	@Test
	public void givenUser_whenDeactivateByAdmin_thenReturnOk() throws Exception {
		// GIVEN, WHEN
		User user = new User("test@test.com", "123", "test", "test", null, 0, "ROLE_USER", true, null, null, null, null,
				null);
		given(userService.getUserByEmail("test@test.com")).willReturn(user);
		// THEN
		mockMvc.perform(
				put("/admin/deactivate").param("email", "test@test.com").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());

	}

	@Test
	public void givenUser_whenSendMoneyToBank_thenReturnOk() throws Exception {
		// GIVEN, WHEN
		User user = new User("test@test.com", "123", "test", "test", null, 100.00, "ROLE_USER", true, null, null, null,
				null, null);
		user.setId((long) 1);
		given(userService.get((long) 1)).willReturn(user);
		BankAccount bankAccount = new BankAccount("my bank", "123456789");
		bankAccount.setResponseFromBankApi(true);
		Double amountTransfered = 100.00;
		String description = "send to bank";
		// THEN
		mockMvc.perform(post("/user/sendToBank").param("amountTransfered", amountTransfered.toString())
				.param("description", description).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

	}

	@Test
	public void givenUser_whenSendMoneyToFriend_thenReturnOk() throws Exception {
		// GIVEN, WHEN
		User user = new User("test@test.com", "123", "test", "test", null, 100.00, "ROLE_USER", true, null, null, null,
				null, null);
		user.setId((long) 1);
		User user2 = new User("x@x.com", "123", "x", "x", null, 0, "ROLE_ADMIN", true, null, null, null, null, null);
		user2.setId((long) 2);
		List<User> moneyFriends = new ArrayList<>();
		moneyFriends.add(user2);
		user.setMoneyFriends(moneyFriends);
		given(userService.get((long) 1)).willReturn(user);
		given(userService.getUserByEmail("x@x.com")).willReturn(user2);
		Double amountAsked = 100.00;
		String description = "send to friend";
		// THEN
		mockMvc.perform(post("/user/sendMoneyToFriend").param("moneyFriendEmail", user2.getEmail())
				.param("amountAsked", amountAsked.toString()).param("description", description)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

	}

	@Test
	public void givenNewUser_whenCreateUser_thenReturnOk() throws Exception {
		// GIVEN, WHEN
		User newUser = new User("test@test.com", "123", "test", "test", null, 0, "ROLE_USER", true, null, null, null,
				null, null);
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(newUser);
		// THEN
		mockMvc.perform(post("/new").content(json).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	}

}
