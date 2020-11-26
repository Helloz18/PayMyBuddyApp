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

import com.PMB.PayMyBuddy.Service.BankAccountService;
import com.PMB.PayMyBuddy.Service.MoneyTransferService;
import com.PMB.PayMyBuddy.Service.UserService;
import com.PMB.PayMyBuddy.model.Address;
import com.PMB.PayMyBuddy.model.BankAccount;
import com.PMB.PayMyBuddy.model.MoneyTransfer;
import com.PMB.PayMyBuddy.model.PhoneNumber;
import com.PMB.PayMyBuddy.model.User;

@RestController
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private BankAccountService bankService;
	
	@Autowired
	private MoneyTransferService moneyTransferService;
	
	@GetMapping("/user/")
	public User getUser() {
		return userService.getConnectedUser();
	}
	
	@GetMapping("/user/myMoneyTransfers")
	public List<MoneyTransfer> getUserTransfer() {
		User user = userService.getConnectedUser();
		return user.getMoneyTransfers();
	}
	
	
	@GetMapping("/admin/all")
	public List<User> getAllUsers() {
		return userService.findAll();
	}
	
		
	@PostMapping("/user/moneyFriends")
	public ResponseEntity<User> addMoneyFriend(@RequestParam String moneyFriendEmail) {
		User user = userService.getConnectedUser();
		userService.addMoneyFriend(user, moneyFriendEmail);
		user = userService.save(user);
		return ResponseEntity.ok(user);
	}
	
	@DeleteMapping("/user/moneyFriends")
	public ResponseEntity<User> deleteMoneyFriend(@RequestParam String moneyFriendEmail) {
		User user = userService.getConnectedUser();
		userService.deleteMoneyFriend(user, moneyFriendEmail);
		user = userService.save(user);
		return ResponseEntity.ok(user);
	}

	
	@PostMapping("/new")
	public ResponseEntity<User> createUser(@RequestBody User user) {
		userService.save(user);
		return ResponseEntity.ok(user);		
	}
	
	@PutMapping("/admin/deactivate")
	public void deActivateUserByAdmin(@RequestParam String email) {
		User user = userService.getUserByEmail(email);
		userService.deactivate(user);
		userService.save(user);
	}
	
	@PutMapping("/user/deactivate")
	public void deActivateUser() {
		User user = userService.getConnectedUser();
		userService.deactivate(user);
		userService.save(user);
	}
	
	@PostMapping("/user/moneyTransfer")
	public ResponseEntity<User> getMoneyFromBank(
			@RequestParam Double amountAsked, @RequestParam String description) {
		User user = userService.getConnectedUser();
		moneyTransferService.processTransferFromBank(user, amountAsked, description);
		userService.save(user);
		return ResponseEntity.ok(user);
	}
	
	@PostMapping("/user/sendToBank")
	public ResponseEntity<User> sendMoneyToBank(
			@RequestParam Double amountTransfered, @RequestParam String description) {
		User user = userService.getConnectedUser();
		moneyTransferService.processTransferToBank(user, amountTransfered, description);
		userService.save(user);
		return ResponseEntity.ok(user);
	}
	
	@PostMapping("/user/sendMoneyToFriend")
	public ResponseEntity<User> sendMoneyToFriend(@RequestParam String moneyFriendEmail,
			@RequestParam Double amountAsked, @RequestParam String description) {
		User user = userService.getConnectedUser();
		moneyTransferService.processTransferToFriend(user, moneyFriendEmail, amountAsked, description);
		userService.save(user);
		return ResponseEntity.ok(user);
	}
	
	
	@PostMapping("/user/bankAccount")
	public ResponseEntity<User> addBankAccount(@RequestBody BankAccount bankAccount){
		User user = userService.getConnectedUser();
		bankService.saveBankAccount(user, bankAccount);
		userService.save(user);
		return ResponseEntity.ok(user);		
	}
	
	@PostMapping("/user/address")
	public ResponseEntity<User> addAddress(@RequestBody Address address){
		User user = userService.getConnectedUser();
		userService.addAddress(user, address);
		userService.save(user);
		return ResponseEntity.ok(user);		
	}
	
	@PostMapping("/user/phone")
	public ResponseEntity<User> addPhone(@RequestBody PhoneNumber phone){
		User user = userService.getConnectedUser();
		userService.addPhoneNumber(user, phone);
		userService.save(user);
		return ResponseEntity.ok(user);		
	}
}
