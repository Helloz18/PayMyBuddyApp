package com.PMB.PayMyBuddy.controller;

import java.net.URI;
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

import com.PMB.PayMyBuddy.Service.UserService;
import com.PMB.PayMyBuddy.model.MoneyTransfer;
import com.PMB.PayMyBuddy.model.User;

@RestController
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/")
	public User getUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		return userService.getUserByEmail(userDetails.getUsername());
	}
	
	@GetMapping("/admin/all")
	public List<User> getAllUsers() {
		return userService.findAll();
	}
	
	@GetMapping("/allMoneyTransfers")
	public List<MoneyTransfer> getUserTransfer() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		return userService.getUserByEmail(userDetails.getUsername()).getMoneyTransfers();
	}
	
	@PutMapping("/user/{user_id}")
	public ResponseEntity<User> addMoneyFriend(@PathVariable Long user_id, @RequestParam String moneyFriendEmail) {
		User user = userService.get(user_id);
		userService.addMoneyFriend(user, moneyFriendEmail);
		user = userService.save(user);
		 return ResponseEntity.ok(user);
	}

	@PostMapping("/user/new")
	public ResponseEntity<User> createUser(@RequestBody User user) {
		userService.save(user);
		return ResponseEntity.ok(user);		
	}
	
	@DeleteMapping("/user/{user_id}")
	public void deleteUser(@PathVariable Long user_id) {
		User user = userService.get(user_id);
		userService.delete(user);
	}
}
