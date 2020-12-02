package com.PMB.PayMyBuddy.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.PMB.PayMyBuddy.model.Address;
import com.PMB.PayMyBuddy.model.BankAccount;
import com.PMB.PayMyBuddy.model.MoneyTransfer;
import com.PMB.PayMyBuddy.model.PhoneNumber;
import com.PMB.PayMyBuddy.model.User;
import com.PMB.PayMyBuddy.service.BankAccountService;
import com.PMB.PayMyBuddy.service.MoneyTransferService;
import com.PMB.PayMyBuddy.service.UserService;

@RestController
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private BankAccountService bankService;

	@Autowired
	private MoneyTransferService moneyTransferService;

	/**
	 * This url allows to register a new user.
	 * 
	 * @param user
	 * @return
	 */
	@PostMapping("/new")
	public ResponseEntity<User> createUser(@RequestBody User user) {
		userService.save(user);
		return ResponseEntity.ok(user);
	}

	/**
	 * When a user is connected, this url shows all his information.
	 * 
	 * @return
	 */
	@GetMapping("/user/")
	public User getUser() {
		return userService.getConnectedUser();
	}

	/**
	 * This url allows the connected user to add his bank account to his profile.
	 * 
	 * @param bankAccount
	 * @return
	 */
	@PostMapping("/user/bankAccount")
	public ResponseEntity<User> addBankAccount(@RequestBody BankAccount bankAccount) {
		User user = userService.getConnectedUser();
		bankService.saveBankAccount(user, bankAccount);
		userService.save(user);
		return ResponseEntity.ok(user);
	}

	/**
	 * This url allows the connected user to add an address to his profile.
	 * 
	 * @param address
	 * @return
	 */
	@PostMapping("/user/address")
	public ResponseEntity<User> addAddress(@RequestBody Address address) {
		User user = userService.getConnectedUser();
		userService.addAddress(user, address);
		userService.save(user);
		return ResponseEntity.ok(user);
	}

	/**
	 * This url allows the connected user to add a phone to his profile.
	 * 
	 * @param phone
	 * @return
	 */
	@PostMapping("/user/phone")
	public ResponseEntity<User> addPhone(@RequestBody PhoneNumber phone) {
		User user = userService.getConnectedUser();
		userService.addPhoneNumber(user, phone);
		userService.save(user);
		return ResponseEntity.ok(user);
	}

	/**
	 * This url send the information of a new friend to add in the friend's list of
	 * the connected user.
	 * 
	 * @param moneyFriendEmail
	 * @return
	 */
	@PostMapping("/user/moneyFriends")
	public ResponseEntity<User> addMoneyFriend(@RequestParam String moneyFriendEmail) {
		User user = userService.getConnectedUser();
		userService.addMoneyFriend(user, moneyFriendEmail);
		user = userService.save(user);
		return ResponseEntity.ok(user);
	}

	/**
	 * This url allows the connected user to receive money from his bank.
	 * 
	 * @param amountAsked
	 * @param description
	 * @return
	 */
	@PostMapping("/user/moneyTransfer")
	public ResponseEntity<User> getMoneyFromBank(@RequestParam Double amountAsked, @RequestParam String description) {
		User user = userService.getConnectedUser();
		moneyTransferService.processTransferFromBank(user, amountAsked, description);
		userService.save(user);
		return ResponseEntity.ok(user);
	}

	/**
	 * This url allows the connected user to send money to his bank.
	 * 
	 * @param amountTransfered
	 * @param description
	 * @return
	 */
	@PostMapping("/user/sendToBank")
	public ResponseEntity<User> sendMoneyToBank(@RequestParam Double amountTransfered,
			@RequestParam String description) {
		User user = userService.getConnectedUser();
		moneyTransferService.processTransferToBank(user, amountTransfered, description);
		userService.save(user);
		return ResponseEntity.ok(user);
	}

	/**
	 * This url allows the connected user to send money to a friend.
	 * 
	 * @param moneyFriendEmail
	 * @param amountAsked
	 * @param description
	 * @return
	 */
	@PostMapping("/user/sendMoneyToFriend")
	public ResponseEntity<User> sendMoneyToFriend(@RequestParam String moneyFriendEmail,
			@RequestParam Double amountAsked, @RequestParam String description) {
		User user = userService.getConnectedUser();
		moneyTransferService.processTransferToFriend(user, moneyFriendEmail, amountAsked, description);
		userService.save(user);
		return ResponseEntity.ok(user);
	}

	/**
	 * This url delete a friend from the friend's list of the connected user.
	 * 
	 * @param moneyFriendEmail
	 * @return
	 */
	@DeleteMapping("/user/moneyFriends")
	public ResponseEntity<User> deleteMoneyFriend(@RequestParam String moneyFriendEmail) {
		User user = userService.getConnectedUser();
		userService.deleteMoneyFriend(user, moneyFriendEmail);
		user = userService.save(user);
		return ResponseEntity.ok(user);
	}

	/**
	 * Page to see the list of MoneyTransfers of the connected user.
	 * 
	 * @return
	 */
	@GetMapping("/user/myMoneyTransfers")
	public List<MoneyTransfer> getUserTransfer() {
		User user = userService.getConnectedUser();
		return user.getMoneyTransfers();
	}

	/**
	 * Admin page to see the list of all the users registered in the application.
	 * 
	 * @return
	 */
	@GetMapping("/admin/all")
	public List<User> getAllUsers() {
		return userService.findAll();
	}

	/**
	 * This url is accessible for the Role ADMIN. It allows to deactivate a user.
	 * 
	 * @param email
	 */
	@PutMapping("/admin/deactivate")
	public void deActivateUserByAdmin(@RequestParam String email) {
		User user = userService.getUserByEmail(email);
		userService.deactivate(user);
		userService.save(user);
	}

	/**
	 * This url allows the connected user to deactivate his account.
	 */
	@PutMapping("/user/deactivate")
	public void deActivateUser() {
		User user = userService.getConnectedUser();
		userService.deactivate(user);
		userService.save(user);
	}

}
