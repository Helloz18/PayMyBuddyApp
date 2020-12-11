package com.PMB.PayMyBuddy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.PMB.PayMyBuddy.model.PhoneNumber;

@Repository
public interface PhoneNumberRepository extends JpaRepository <PhoneNumber, Long> {

}
