package com.PMB.PayMyBuddy.Service;


import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.PMB.PayMyBuddy.exception.UserNotFoundException;
import com.PMB.PayMyBuddy.model.User;
import com.PMB.PayMyBuddy.repository.UserRepository;

@Service
@Transactional
public class UserService {
	
	final Logger LOGGER = LoggerFactory.getLogger(UserService.class);		
		
	@Autowired
	private UserRepository userRepo;
	

	public User get(Long id) {
		return userRepo.getOne(id);
	}

	public User getUserByEmail(String email) {
		return userRepo.findByEmail(email);
	}

	public List<User> findAll() {
		return userRepo.findAll();
	}
	
	public void addMoneyFriend(User user, String moneyFriendEmail) {		
		User moneyFriend = userRepo.findByEmail(moneyFriendEmail);		
		try {
			if(!moneyFriend.getEmail().equals(null)
					&& moneyFriend.isEnabled()) {
				List<User> list = user.getMoneyFriends();
				if(list == null) {
					list = new ArrayList<>();
				}
				if(list.contains(moneyFriend)) {
					LOGGER.error("user "+moneyFriendEmail+" is already a money friend.");
				}else {
				list.add(moneyFriend);
				user.setMoneyFriends(list);}
			}else {
				LOGGER.error("no user exists or is enabled with email="+moneyFriendEmail+".");
				throw new UserNotFoundException("wrong email.");
			}
		}catch (UserNotFoundException e) {
			e.printStackTrace();			
		}				
	}
	
	
	public User save(User user) {
		return userRepo.save(user);
	}
	
	public void delete(User user) {
		userRepo.delete(user);
	}
	
	
	
	
}
