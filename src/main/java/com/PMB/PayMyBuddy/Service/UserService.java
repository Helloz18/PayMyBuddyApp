package com.PMB.PayMyBuddy.Service;


import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.PMB.PayMyBuddy.exception.UserNotFoundException;
import com.PMB.PayMyBuddy.model.Address;
import com.PMB.PayMyBuddy.model.PhoneNumber;
import com.PMB.PayMyBuddy.model.User;
import com.PMB.PayMyBuddy.repository.AddressRepository;
import com.PMB.PayMyBuddy.repository.PhoneNumberRepository;
import com.PMB.PayMyBuddy.repository.UserRepository;

@Service
@Transactional
public class UserService {
	
	final Logger LOGGER = LoggerFactory.getLogger(UserService.class);		
		
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private AddressRepository addressRepo;
	
	@Autowired
	private PhoneNumberRepository phoneRepo;
	

	public User get(Long id) {
		return userRepo.getOne(id);
	}
	
	public User save(User user) {
		try {
			userRepo.save(user);
		}catch(Exception e) {
			LOGGER.error("Le user avec l'email : "+user.getEmail()+" existe déjà.");
			e.printStackTrace();		
		}
		return user;
	}
	
	public void deactivate(User user) {
		user.setEnabled(false);
	}
	
	public void delete(User user) {
		userRepo.delete(user);
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
			if(moneyFriend != null
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
	
	
	

	public void deleteMoneyFriend(User user, String moneyFriendEmail) {
		User moneyFriend = userRepo.findByEmail(moneyFriendEmail);		
		try {
			if(moneyFriend != null
					&& moneyFriend.isEnabled()) {
				List<User> list = user.getMoneyFriends();
				if(list.contains(moneyFriend)) {
					list.remove(moneyFriend);
					user.setMoneyFriends(list);
				}else {
					LOGGER.error("no user exists in the user friends list or is enabled with email="+moneyFriendEmail+".");
					throw new UserNotFoundException("user is not in the friends list.");
				}
			}else {
				LOGGER.error("no user exists or is enabled with email="+moneyFriendEmail+".");
				throw new UserNotFoundException("wrong email.");
			
			}
		}catch (UserNotFoundException e) {
			e.printStackTrace();			
		}
		
	}
	
	public void addAddress(User user, Address address) {
		List<Address> addresses = user.getAddresses();
		if(addresses == null) {
			addresses = new ArrayList<>();
		}
		addresses.add(address);
		user.setAddresses(addresses);
		addressRepo.save(address);		
	}
	
	public void addPhoneNumber(User user, PhoneNumber phone) {
		List<PhoneNumber> phoneNumbers = user.getPhoneNumbers();
		if(phoneNumbers == null) {
			phoneNumbers = new ArrayList<>();
		}
		phoneNumbers.add(phone);
		user.setPhoneNumbers(phoneNumbers);
		phoneRepo.save(phone);
	}
	
	public void changeAddress(User user, long address_id, Address changedAddress) {
		List<Address> addresses = user.getAddresses();
		for(int i=0; i<addresses.size(); i++) {
			if(addresses.get(i).getId() == address_id) {
				Address addressToChange = addressRepo.getOne(address_id);
				addressToChange.setNumber(changedAddress.getNumber());
				addressToChange.setStreet(changedAddress.getStreet());
				addressToChange.setZip(changedAddress.getZip());
				addressToChange.setCity(changedAddress.getCity());
				addressRepo.save(addressToChange);
			}
		}	
	}
	
	public void changePhoneNumber(User user, long phone_id, PhoneNumber changedPhoneNumber) {
		List<PhoneNumber> phonenumbers = user.getPhoneNumbers();
		for(int i=0; i<phonenumbers.size(); i++) {
			if(phonenumbers.get(i).getId() == phone_id) {
				PhoneNumber phoneToChange = phoneRepo.getOne(phone_id);
				phoneToChange.setNumber(changedPhoneNumber.getNumber());
				phoneToChange.setName(changedPhoneNumber.getName());
				phoneRepo.save(phoneToChange);
			}
		}	
	}
	
	public void deleteAddress(User user, long address_id) {
		List<Address> addresses = user.getAddresses();
		for(int i=0; i<addresses.size(); i++) {
			if(addresses.get(i).getId() == address_id) {
				Address addressToDelete = addressRepo.getOne(address_id);
				addresses.remove(addresses.get(i));
				addressRepo.delete(addressToDelete);
			}
		}
	}
	
	public void deletePhoneNumber(User user, long phone_id) {
		List<PhoneNumber> phonenumbers = user.getPhoneNumbers();
		for(int i=0; i<phonenumbers.size(); i++) {
			if(phonenumbers.get(i).getId() == phone_id) {
				PhoneNumber phoneToDelete = phoneRepo.getOne(phone_id);
				phonenumbers.remove(phonenumbers.get(i));
				phoneRepo.delete(phoneToDelete);
			}
		}
	}
}
