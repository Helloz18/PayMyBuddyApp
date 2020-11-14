package com.PMB.PayMyBuddy.controller;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.PMB.PayMyBuddy.PayMyBuddyApplication;

@RestController
public class LoginController {
	
	final Logger LOGGER = LoggerFactory.getLogger(PayMyBuddyApplication.class);	
	
	private boolean isAuthenticated() {
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    if (authentication == null || AnonymousAuthenticationToken.class.
	      isAssignableFrom(authentication.getClass())) {
	        return false;
	    }
	    return authentication.isAuthenticated();
	}
	
//	@RequestMapping("/")
//	public String getUser() {
//		return "redirect:/user/";
//	}

	@RequestMapping("/admin")
	public String getAdmin() {
		return "admin page";
	}
	
//	@GetMapping("/")
//	public String getUserLoginPage() {
//	    if (isAuthenticated()) {
//	        return "/user/";
//	    }
//	    return "/";
//	}

}
