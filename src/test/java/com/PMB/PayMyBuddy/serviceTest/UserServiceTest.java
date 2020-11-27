package com.PMB.PayMyBuddy.serviceTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
import com.PMB.PayMyBuddy.model.Address;
import com.PMB.PayMyBuddy.model.PhoneNumber;
import com.PMB.PayMyBuddy.model.User;
import com.PMB.PayMyBuddy.repository.AddressRepository;
import com.PMB.PayMyBuddy.repository.PhoneNumberRepository;
import com.PMB.PayMyBuddy.repository.UserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PayMyBuddyApplication.class)
public class UserServiceTest {

	@Autowired
	private UserService userService;

	@MockBean
	private UserRepository userRepository;

	@MockBean
	private AddressRepository addressRepo;

	@MockBean
	private PhoneNumberRepository phoneRepo;

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

		Mockito.when(userRepository.findByEmail(user.getEmail())).thenReturn(user);
		Mockito.when(userRepository.getOne(user.getId())).thenReturn(user);

	}

	@Test
	public void whenValidEmailThenUSerShouldBeFound() {
		// GIVEN
		String email = "test@test.com";
		// WHEN
		User found = userService.getUserByEmail(email);
		// THEN
		assertThat(found.getFirstname()).isEqualTo("test");
	}

	@Test
	public void whenValidIdThenUSerShouldBeFound() {
		// GIVEN
		String email = "test@test.com";
		User user = userRepository.findByEmail(email);
		// WHEN
		User found = userService.get(user.getId());
		// THEN
		assertThat(found.getFirstname()).isEqualTo("test");
	}

	@Test
	public void testGeListUsersReturnsFindAllUsersFromRepository() {

		User user = new User("test@test.com", "1234", "test", "test", null, 0, "ROLE_USER", true, null, null, null,
				null, null);
		User user2 = new User("x@x.com", "1234", "x", "x", null, 0, "ROLE_ADMIN", true, null, null, null, null, null);
		List<User> users = java.util.Arrays.asList(user, user2);
		when(userRepository.findAll()).thenReturn(users);

		// Act
		List<User> result = userService.findAll();
		// Assert
		assertTrue(result.size() == 2);
		assertTrue(result.get(0).getFirstname().equals("test"));
		assertTrue(result.get(1).getFirstname().equals("x"));
	}

	@Test
	public void whenAddNewFriendThenNewFriendIsInUserMoneyFriends() {
		// GIVEN
		User friend = new User("friend@test.com", "1234", "ami", "son nom", null, 0, "ROLE_USER", true, null, null,
				null, null, null);
		String email = "test@test.com";

		Mockito.when(userRepository.findByEmail(friend.getEmail())).thenReturn(friend);

		User user = userRepository.findByEmail(email);
		// WHEN
		userService.addMoneyFriend(user, friend.getEmail());
		// THEN
		assertThat(user.getMoneyFriends().contains(friend));

	}

	@Test
	public void whenAddNewFriendIsDisabledThenNewFriendIsNotAdded() {
		// GIVEN
		User friend = new User("friend@test.com", "1234", "ami", "son nom", null, 0, "ROLE_USER", false, null, null,
				null, null, null);
		Mockito.when(userRepository.findByEmail(friend.getEmail())).thenReturn(friend);

		String email = "test@test.com";
		User user = userRepository.findByEmail(email);

		// WHEN
		userService.addMoneyFriend(user, friend.getEmail());
		// THEN
		assertEquals(null, user.getMoneyFriends());
	}

	@Test
	public void whenDeleteFriendThenDeletedFriendIsNotInUserMoneyFriends() {
		// GIVEN
		User friend = new User("friend@test.com", "1234", "ami", "son nom", null, 0, "ROLE_USER", true, null, null,
				null, null, null);
		Mockito.when(userRepository.findByEmail(friend.getEmail())).thenReturn(friend);
		List<User> friends = new ArrayList<>();
		friends.add(friend);
		String email = "test@test.com";
		User user = userRepository.findByEmail(email);
		user.setMoneyFriends(friends);
		// WHEN
		userService.deleteMoneyFriend(user, friend.getEmail());
		// THEN
		assertTrue(user.getMoneyFriends().size() == 0);
	}

	@Test
	public void whenAddWrongEmailForFriendThenNoNewMoneyFriendIsAdded() {
		// GIVEN
		String email = "test@test.com";
		User user = userRepository.findByEmail(email);
		// WHEN
		userService.addMoneyFriend(user, "wrongemail.test.com");
		// THEN
		assertEquals(null, user.getMoneyFriends());

	}

	@Test
	public void whenEmptyThenNoMoneyFriendIsAdded() {
		// GIVEN
		String email = "test@test.com";
		User user = userRepository.findByEmail(email);
		// WHEN
		userService.addMoneyFriend(user, "");
		// THEN
		assertEquals(null, user.getMoneyFriends());
	}

	@Test
	public void whenDelete_but_noMoneyFriend_thenException() {
		// GIVEN
		String email = "test@test.com";
		User user = userRepository.findByEmail(email);
		// WHEN
		userService.deleteMoneyFriend(user, null);
		// THEN
		assertEquals(null, user.getMoneyFriends());
	}

	@Test
	public void whenDeleteFriend_butNotPresentInMoneyFriends() {
		// GIVEN
		User friend = new User("friend@test.com", "1234", "ami", "son nom", null, 0, "ROLE_USER", true, null, null,
				null, null, null);
		Mockito.when(userRepository.findByEmail(friend.getEmail())).thenReturn(friend);
		List<User> friends = new ArrayList<>();
		friends.add(friend);
		String email = "test@test.com";
		User user = userRepository.findByEmail(email);
		user.setMoneyFriends(friends);
		// WHEN
		userService.deleteMoneyFriend(user, email);
		// THEN
		assertEquals(user.getMoneyFriends().size(), 1);
	}

	@Test
	public void whenAddNewAddressThenNewAddressIsInUserAddresses() {
		// GIVEN
		String email = "test@test.com";
		User user = userRepository.findByEmail(email);
		Address address = new Address("1", "first street", "01000", "MainCity");
		// WHEN
		userService.addAddress(user, address);
		// THEN
		assertEquals(user.getAddresses().size(), 1);
	}

	@Test
	public void whenAddNewPhoneNumberThenNewPhoneNumberIsInUserPhoneNumbers() {
		// GIVEN
		String email = "test@test.com";
		User user = userRepository.findByEmail(email);
		PhoneNumber phone = new PhoneNumber("home", "1234567890");
		// WHEN
		userService.addPhoneNumber(user, phone);
		// THEN
		assertEquals(user.getPhoneNumbers().size(), 1);

	}

	@Test
	public void whenChangeAddressThenAddressIsChangedInUserAddresses() {
		// GIVEN
		String email = "test@test.com";
		User user = userRepository.findByEmail(email);
		Address address = new Address("1", "first street", "01000", "MainCity");
		address.setId(1);
		Mockito.when(addressRepo.getOne((long) 1)).thenReturn(address);
		List<Address> list = new ArrayList<>();
		list.add(address);
		user.setAddresses(list);
		Address newAddress = new Address("5", "second streed", "02000", "NewCity");
		// WHEN
		userService.changeAddress(user, address.getId(), newAddress);
		// THEN
		assertEquals(user.getAddresses().get(0).getCity(), "NewCity");
	}

	@Test
	public void whenChangePhoneThenPhoneIsChangedInUserPhones() {
		// GIVEN
		String email = "test@test.com";
		User user = userRepository.findByEmail(email);
		PhoneNumber phone = new PhoneNumber("home", "1234567890");
		phone.setId(1);
		Mockito.when(phoneRepo.getOne((long) 1)).thenReturn(phone);
		List<PhoneNumber> list = new ArrayList<>();
		list.add(phone);
		user.setPhoneNumbers(list);
		PhoneNumber newPhone = new PhoneNumber("work", "0987654321");
		// WHEN
		userService.changePhoneNumber(user, phone.getId(), newPhone);
		// THEN
		assertEquals(user.getPhoneNumbers().get(0).getName(), "work");
	}

	@Test
	public void whenDeleteAddressThenAddressIsNotInUserAddresses() {
		// GIVEN
		String email = "test@test.com";
		User user = userRepository.findByEmail(email);
		Address address = new Address("1", "first street", "01000", "MainCity");
		address.setId(1);
		Mockito.when(addressRepo.getOne((long) 1)).thenReturn(address);
		List<Address> list = new ArrayList<>();
		list.add(address);
		user.setAddresses(list);
		// WHEN
		userService.deleteAddress(user, address.getId());
		// THEN
		assertEquals(0, user.getAddresses().size());
	}

	@Test
	public void whenDeletePhoneThenPhoneIsDeletedInUserPhones() {
		// GIVEN
		String email = "test@test.com";
		User user = userRepository.findByEmail(email);
		PhoneNumber phone = new PhoneNumber("home", "1234567890");
		phone.setId(1);
		Mockito.when(phoneRepo.getOne((long) 1)).thenReturn(phone);
		List<PhoneNumber> list = new ArrayList<>();
		list.add(phone);
		user.setPhoneNumbers(list);
		// WHEN
		userService.deletePhoneNumber(user, phone.getId());
		// THEN
		assertEquals(0, user.getPhoneNumbers().size());
	}

}
