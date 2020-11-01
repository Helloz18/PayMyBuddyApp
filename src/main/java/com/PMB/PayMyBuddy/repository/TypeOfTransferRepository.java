package com.PMB.PayMyBuddy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.PMB.PayMyBuddy.model.MoneyTransfer;
import com.PMB.PayMyBuddy.model.TypeOfTransfer;

@Repository
public interface TypeOfTransferRepository extends JpaRepository<TypeOfTransfer, Integer> {

}
