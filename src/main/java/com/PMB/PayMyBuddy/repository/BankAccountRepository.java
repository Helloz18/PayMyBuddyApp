package com.PMB.PayMyBuddy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.PMB.PayMyBuddy.model.BankAccount;

@Repository
public interface BankAccountRepository extends JpaRepository <BankAccount, Long> {

}
