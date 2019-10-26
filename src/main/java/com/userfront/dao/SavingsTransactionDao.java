package com.userfront.dao;

import org.springframework.data.repository.CrudRepository;

import com.userfront.domain.SavingsAccount;
import com.userfront.domain.SavingsTransaction;

public interface SavingsTransactionDao extends CrudRepository<SavingsTransaction, Long> {
	SavingsTransaction findBySavingsAccount(SavingsAccount savingsAccount);

}
