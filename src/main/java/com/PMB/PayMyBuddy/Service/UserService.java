package com.PMB.PayMyBuddy.Service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.PMB.PayMyBuddy.model.User;
import com.PMB.PayMyBuddy.repository.UserRepository;

@Service
@Transactional
public class UserService {

    private UserRepository userRepo;

    
		
	//@Autowired
	//private UserRepository userRepo;
	
    @Autowired
	public UserService(UserRepository userRepo) {
		super();
		this.userRepo = userRepo;
	}


	public User get(long id) {
		return userRepo.findById(id).get();
	}

	public User getUserByEmail(String email) {
		return userRepo.findByEmail(email);
	}

	public List<User> findAll() {
		return userRepo.findAll();
	}
}
