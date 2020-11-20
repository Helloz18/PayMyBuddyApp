package com.PMB.PayMyBuddy.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.PMB.PayMyBuddy.Service.BankAccountService;
import com.PMB.PayMyBuddy.Service.MoneyTransferService;
import com.PMB.PayMyBuddy.Service.UserService;
import com.PMB.PayMyBuddy.model.BankAccount;
import com.PMB.PayMyBuddy.model.MoneyTransfer;
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
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		return userService.getUserByEmail(userDetails.getUsername());
	}
	
	@GetMapping("/user/myMoneyTransfers")
	public List<MoneyTransfer> getUserTransfer() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		String username = userDetails.getUsername();
		return userService.getUserByEmail(username).getMoneyTransfers();
	}
	
	
	@GetMapping("/admin/all")
	public List<User> getAllUsers() {
		return userService.findAll();
	}
	
		
	@PostMapping("/user/moneyFriends")
	public ResponseEntity<User> addMoneyFriend(@RequestParam String moneyFriendEmail) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		String username = userDetails.getUsername();
		User user = userService.getUserByEmail(username);
		userService.addMoneyFriend(user, moneyFriendEmail);
		user = userService.save(user);
		return ResponseEntity.ok(user);
	}
	
	@DeleteMapping("/user/moneyFriends")
	public ResponseEntity<User> deleteMoneyFriend(@RequestParam String moneyFriendEmail) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		String username = userDetails.getUsername();
		User user = userService.getUserByEmail(username);
		userService.deleteMoneyFriend(user, moneyFriendEmail);
		user = userService.save(user);
		return ResponseEntity.ok(user);
	}

	
	@PostMapping("/new")
	public ResponseEntity<User> createUser(@RequestBody User user) {
		userService.save(user);
		return ResponseEntity.ok(user);		
	}
	
	@DeleteMapping("/admin/{user_id}")
	public void deleteUser(@PathVariable Long user_id) {
		User user = userService.get(user_id);
		userService.delete(user);
	}
	
	@PutMapping("/user/deactivate")
	public void deActivateUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		String username = userDetails.getUsername();
		User user = userService.getUserByEmail(username);
		userService.deactivate(user);
		userService.save(user);
	}
	
	@PostMapping("/user/moneyTransfer")
	public ResponseEntity<User> getMoneyFromBank(
			@RequestParam Double amountAsked, @RequestParam String description) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		String username = userDetails.getUsername();
		User user = userService.getUserByEmail(username);
		moneyTransferService.processTransferFromBank(user, amountAsked, description);
		userService.save(user);
		return ResponseEntity.ok(user);
	}
	
	@PostMapping("/user/sendMoneyToFriend")
	public ResponseEntity<User> sendMoneyToFriend(@RequestParam String moneyFriendEmail,
			@RequestParam Double amountAsked, @RequestParam String description) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		String username = userDetails.getUsername();
		User user = userService.getUserByEmail(username);
		moneyTransferService.processTransferToFriend(user, moneyFriendEmail, amountAsked, description);
		userService.save(user);
		return ResponseEntity.ok(user);
	}
	
	
	@PostMapping("/user/bankAccount")
	public ResponseEntity<User> addBankAccount(@RequestBody BankAccount bankAccount){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		String username = userDetails.getUsername();
		User user = userService.getUserByEmail(username);
		bankService.saveBankAccount(user, bankAccount);
		userService.save(user);
		return ResponseEntity.ok(user);		
	}
}
