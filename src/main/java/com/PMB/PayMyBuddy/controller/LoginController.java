package com.PMB.PayMyBuddy.controller;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.PMB.PayMyBuddy.PayMyBuddyApplication;

@RestController
public class LoginController {
	
	@RequestMapping("/")
	public String getUser() {
		final Logger LOGGER = LoggerFactory.getLogger(PayMyBuddyApplication.class);	
		LOGGER.info("accès à la user's page");
		LOGGER.warn("test warn");
		LOGGER.error("test error");
		
		return "user page";
	}

	@RequestMapping("/admin")
	public String getAdmin() {
		return "admin page";
	}

}
