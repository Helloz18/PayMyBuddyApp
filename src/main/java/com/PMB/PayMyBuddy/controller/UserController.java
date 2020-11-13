package com.PMB.PayMyBuddy.controller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.PMB.PayMyBuddy.Service.UserService;
import com.PMB.PayMyBuddy.model.MoneyTransfer;
import com.PMB.PayMyBuddy.model.User;

@RestController
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/user/{user_id}")
	public User getUser(@PathVariable Long user_id) {
		return userService.get(user_id);
	}
	
	@GetMapping("/user/all")
	public List<User> getAllUsers() {
		return userService.findAll();
	}
	
	@GetMapping("/user/{user_id}/allMoneyTransfers")
	public List<MoneyTransfer> getUserTransfer(@PathVariable Long user_id) {
		return userService.get(user_id).getMoneyTransfers();
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
