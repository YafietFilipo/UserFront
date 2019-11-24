package com.userfront.service;

import java.security.Principal;
import java.util.List;

import com.userfront.domain.PrimaryAccount;
import com.userfront.domain.PrimaryTransaction;
import com.userfront.domain.Recipient;
import com.userfront.domain.SavingsAccount;
import com.userfront.domain.SavingsTransaction;
import com.userfront.domain.User;

public interface TransactionService {
	List<PrimaryTransaction> findPrimaryTransactionList(String username);
	List<SavingsTransaction> findSavingsTransactionList(String username);
	
	void savePrimaryDepositTransaction(PrimaryTransaction primaryTransaction);
	void saveSavingsDepositTransaction(SavingsTransaction savingsTransaction);
	
	void savePrimaryWithdrawTransaction(PrimaryTransaction primaryTransaction);
	void saveSavingsWithdrawTransaction(SavingsTransaction savigsTransaction);
	void betweenAccountsTransfer(String transferFrom, String transferTo, String amount, PrimaryAccount primaryAccount,
			SavingsAccount savingsAccount)throws Exception;
	List<Recipient> findRecipientList(Principal principal);
	Recipient findRecipientByName(String recipientName);
	void saveRecipient(Recipient recipient);
	void deleteRecipientByName(String recipientName);
	void transferToSomeoneElse(Recipient recipient,String amount,String accountType,PrimaryAccount primaryAccount,SavingsAccount savingsAccount)throws Exception;
}
