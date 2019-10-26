package com.userfront.service.UserServiceImpl;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.userfront.dao.PrimaryAccountDao;
import com.userfront.dao.SavingsAccountDao;
import com.userfront.dao.SavingsTransactionDao;
import com.userfront.domain.PrimaryAccount;
import com.userfront.domain.PrimaryTransaction;
import com.userfront.domain.SavingsAccount;
import com.userfront.domain.SavingsTransaction;
import com.userfront.domain.User;
import com.userfront.service.AccountService;
import com.userfront.service.UserService;

@Service
public class AccountServiceImpl implements AccountService {

	private static int nextAccountNumber = 11223145;

	@Autowired
	private PrimaryAccountDao primaryAccountDao;

	@Autowired
	private SavingsAccountDao savingsAccountDao;

	@Autowired
	private UserService userService;

	// @Autowired
	// private PrimaryTransactionDao primaryTransactionDao;

	@Autowired
	 private SavingsTransactionDao savingsTransactionDao;

	public PrimaryAccount createPrimaryAccount() {
		PrimaryAccount primaryAccount = new PrimaryAccount();
		primaryAccount.setAccountBalance(new BigDecimal(0.00));
		primaryAccount.setAccountNumber(accountGen());

		primaryAccountDao.save(primaryAccount);

		return primaryAccountDao.findByAccountNumber(primaryAccount.getAccountNumber());
	}

	public SavingsAccount createSavingsAccount() {
		SavingsAccount savingsAccount = new SavingsAccount();
		savingsAccount.setAccountBalance(new BigDecimal(0.00));
		savingsAccount.setAccountNumber(accountGen());

		savingsAccountDao.save(savingsAccount);

		savingsAccountDao.findByAccountNumber(savingsAccount.getAccountNumber());
		return savingsAccountDao.findByAccountNumber(savingsAccount.getAccountNumber());

	}

	public int accountGen() {
		return ++nextAccountNumber;
	}

	public void deposit(String accountType, Double amount, Principal principal) {
		User user = userService.findByUsername(principal.getName());
		if (accountType.equalsIgnoreCase("primary")) {
			PrimaryAccount primaryAccount = user.getPrimaryAccount();
			primaryAccount.setAccountBalance(primaryAccount.getAccountBalance().add(new BigDecimal(amount)));
			primaryAccountDao.save(primaryAccount);

			Date date = new Date();

			PrimaryTransaction primaryTransaction = new PrimaryTransaction(date, "Deposit to Primary account",
					"account", "finish", amount, primaryAccount.getAccountBalance(), primaryAccount);
			// primaryTransactionDao.save(primaryTransaction);

		} else if (accountType.equalsIgnoreCase("savings")) {
			SavingsAccount savingsAccount = user.getSavingsAccount();
			savingsAccount.setAccountBalance(savingsAccount.getAccountBalance().add(new BigDecimal(amount)));
			savingsAccountDao.save(savingsAccount);

			Date date = new Date();
			SavingsTransaction savingsTransaction = new SavingsTransaction(date, "Deposit to savings account",
					"account", "finished", amount, savingsAccount.getAccountBalance(), savingsAccount);
			savingsTransactionDao.save(savingsTransaction);

		}

	}

	public void withdraw(String accountType, Double amount, Principal principal) {
		User user = userService.findByUsername(principal.getName());
		if (accountType.equalsIgnoreCase("primary")) {
			PrimaryAccount primaryAccount = user.getPrimaryAccount();
			primaryAccount.setAccountBalance(primaryAccount.getAccountBalance().subtract(new BigDecimal(amount)));
			primaryAccountDao.save(primaryAccount);

			Date date = new Date();
			PrimaryTransaction primaryTransaction = new PrimaryTransaction(date, "withdraw from primary Account",
					"account", "Finished", amount, primaryAccount.getAccountBalance(), primaryAccount);

		} else if (accountType.equalsIgnoreCase("savings")) {
			SavingsAccount savingsAccount = user.getSavingsAccount();
			savingsAccount.setAccountBalance(savingsAccount.getAccountBalance().subtract(new BigDecimal(amount)));
			savingsAccountDao.save(savingsAccount);
			
			Date date = new Date();
			
			SavingsTransaction savingsTransaction = new SavingsTransaction(date,"withdraw form Savings Account","account",
					"Finished",amount,savingsAccount.getAccountBalance(),savingsAccount);
			savingsTransactionDao.save(savingsTransaction);

		}

	}

}
