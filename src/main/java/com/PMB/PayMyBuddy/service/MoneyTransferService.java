package com.PMB.PayMyBuddy.service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.PMB.PayMyBuddy.exception.NotEnoughMoneyException;
import com.PMB.PayMyBuddy.exception.QuotaReachedException;
import com.PMB.PayMyBuddy.model.MoneyTransfer;
import com.PMB.PayMyBuddy.model.TypeOfTransfer;
import com.PMB.PayMyBuddy.model.User;
import com.PMB.PayMyBuddy.repository.MoneyTransferRepository;

@Service
@Transactional
public class MoneyTransferService {

	final Logger LOGGER = LoggerFactory.getLogger(MoneyTransferService.class);

	/**
	 * To choose the value of the amount max of a user can have on his appAccount go
	 * to properties file.
	 */
	@Value("${amountMax}")
	private Double amountMax;

	@Autowired
	MoneyTransferRepository moneyTransferRepository;

	@Autowired
	TypeOfTransferService typeService;

	@Autowired
	BankAccountService bankService;

	@Autowired
	UserService userService;

	@Autowired
	TypeOfTransferService typeOfTransferService;

	/**
	 * This method will get money from bank, then set info for the transfer, then
	 * save the transfer in DB (the user is not saved, the call of this method
	 * should be in userService)
	 * 
	 * @param user
	 * @param amountAsked
	 * @param description
	 * @return a Map which contain the MoneyTransfer and the amount taken from the
	 *         app depending on the type of transfer (it could be 0)
	 */
	public Map<MoneyTransfer, Double> processTransferFromBank(User user, Double amountAsked, String description) {

		Map<MoneyTransfer, Double> map = new HashMap<MoneyTransfer, Double>();
		MoneyTransfer fromBankToUser = new MoneyTransfer();
		Double moneyCollected = 0.00;

		try {
			fromBankToUser.setMoneySender(user);
			fromBankToUser.setMoneyFriend(user);
			fromBankToUser.setDescription(description);
			fromBankToUser.setDate(OffsetDateTime.now());
			TypeOfTransfer type = typeService.getById(3);
			fromBankToUser.setTypeOfTransfer(type);

			Double amountResult = bankService.fundAppAccount(user, amountAsked);
			try {
				if (amountResult <= 0.00) {
					LOGGER.info("no money will be added on appAccount");

				} else {
					Double amount = typeService.amountFromTypeOfTransfer(amountResult, type.getId());
					moneyCollected = typeService.moneyCollected(amountResult, type.getId());
					Double amountTotal = user.getAppAccount() + amount;

					try {
						if (amountTotal <= amountMax) {
							fromBankToUser.setAmount(amountAsked);
							user.setAppAccount(amountTotal);
							List<MoneyTransfer> list = user.getMoneyTransfers();
							if (list == null) {
								list = new ArrayList<>();
							}
							list.add(fromBankToUser);
							user.setMoneyTransfers(list);
						} else {
							LOGGER.error("quota reached, the amountMax=" + amountMax + " will be reached.");
							throw new QuotaReachedException("quota reached exception.");
						}
					} catch (QuotaReachedException e) {
						e.printStackTrace();
					}

					try {
						moneyTransferRepository.save(fromBankToUser);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		map.put(fromBankToUser, moneyCollected);
		return map;
	}

	/**
	 * This method will transfer money from the app account to the bank account of
	 * the user
	 * 
	 * @param user
	 * @param amountTransfered
	 * @param description
	 * @return a Map which contain the MoneyTransfer and the amount taken from the
	 *         app depending on the type of transfer (it could be 0)
	 */
	public Map<MoneyTransfer, Double> processTransferToBank(User user, Double amountTransfered, String description) {

		Map<MoneyTransfer, Double> map = new HashMap<MoneyTransfer, Double>();
		MoneyTransfer fromUserToBank = new MoneyTransfer();
		Double moneyCollected = 0.00;

		try {
			fromUserToBank.setMoneySender(user);
			fromUserToBank.setMoneyFriend(user);
			fromUserToBank.setDescription(description);
			fromUserToBank.setDate(OffsetDateTime.now());
			TypeOfTransfer type = typeService.getById(2);
			fromUserToBank.setTypeOfTransfer(type);

			try {
				Double amountUserOnApp = user.getAppAccount();
				if (amountUserOnApp >= amountTransfered) {
					Double amountToSend = typeService.amountFromTypeOfTransfer(amountTransfered, type.getId());
					moneyCollected = typeService.moneyCollected(amountTransfered, type.getId());
					bankService.fundBankAccount(user, amountToSend);
					Double amountResult = amountUserOnApp - amountTransfered;
					user.setAppAccount(amountResult);
					LOGGER.info("user appAccount is now = " + amountResult + ".");
					fromUserToBank.setAmount(amountTransfered);
				} else {
					LOGGER.error("Trying to transfer more money than is present on app account.");
					throw new NotEnoughMoneyException("not enough money on app account.");
				}

				try {
					moneyTransferRepository.save(fromUserToBank);
				} catch (Exception ex) {
					ex.printStackTrace();
				}

			} catch (NotEnoughMoneyException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		map.put(fromUserToBank, moneyCollected);
		return map;
	}

	/**
	 * This method will transfer money from the app account of a user to another one
	 * 
	 * @param user
	 * @param moneyFriendEmail
	 * @param amountAsked
	 * @param description
	 * @return a Map which contain the MoneyTransfer and the amount taken from the
	 *         app depending on the type of transfer (it could be 0)
	 */
	public Map<MoneyTransfer, Double> processTransferToFriend(User user, String moneyFriendEmail, Double amountAsked,
			String description) {

		Map<MoneyTransfer, Double> map = new HashMap<MoneyTransfer, Double>();
		MoneyTransfer fromUserToFriend = new MoneyTransfer();
		Double moneyCollected = 0.00;

		try {
			User moneyFriend = userService.getUserByEmail(moneyFriendEmail);
			List<User> moneyFriends = user.getMoneyFriends();
			if (moneyFriends.size() > 0 && moneyFriends.contains(moneyFriend) && moneyFriend.isEnabled()) {
				fromUserToFriend.setMoneySender(user);
				fromUserToFriend.setMoneyFriend(moneyFriend);
				fromUserToFriend.setAmount(amountAsked);
				fromUserToFriend.setDate(OffsetDateTime.now());
				TypeOfTransfer type = typeService.getById(1);
				fromUserToFriend.setTypeOfTransfer(type);
				fromUserToFriend.setDescription(description);
				try {
					Double userAccount = user.getAppAccount();
					if (userAccount >= amountAsked) {
						userAccount = userAccount - amountAsked;
						user.setAppAccount(userAccount);

						Double friendAccount = moneyFriend.getAppAccount();

						Double amount = typeService.amountFromTypeOfTransfer(amountAsked, type.getId());
						moneyCollected = typeService.moneyCollected(amountAsked, type.getId());
						Double amountTotal = friendAccount + amount;

						if (amountTotal <= amountMax) {
							moneyFriend.setAppAccount(amountTotal);
						} else {
							LOGGER.error("AmountMax on MoneyFriend " + moneyFriendEmail + " reached.");
						}

						List<MoneyTransfer> list = user.getMoneyTransfers();
						if (list == null) {
							list = new ArrayList<>();
						}
						list.add(fromUserToFriend);
						user.setMoneyTransfers(list);

					} else {
						LOGGER.error("not enough money on app account.");
						throw new NotEnoughMoneyException("not enough money on app account.");
					}

					try {
						moneyTransferRepository.save(fromUserToFriend);
					} catch (Exception e) {
						e.printStackTrace();
					}

				} catch (NotEnoughMoneyException e) {
					e.printStackTrace();
				}
			} else {
				LOGGER.error("the moneyFriend is not in the list, or is not enabled.");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		map.put(fromUserToFriend, moneyCollected);
		return map;
	}
}
