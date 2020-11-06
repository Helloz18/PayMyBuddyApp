package com.PMB.PayMyBuddy.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.PMB.PayMyBuddy.Service.UserService;
import com.PMB.PayMyBuddy.model.MoneyTransfer;
import com.PMB.PayMyBuddy.model.User;

@RestController
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@RequestMapping("/user/{user_id}")
	public User getUser(@PathVariable Long user_id) {
		return userService.get(user_id);
	}
	
	@RequestMapping("/user/all")
	public List<User> getAllUsers() {
		return userService.findAll();
	}
	
	@RequestMapping("/userTransfers/{user_id}")
	public List<MoneyTransfer> getUserTransfer(@PathVariable Long user_id) {
		return userService.get(user_id).getMoneyTransfers();
	}
	
}
