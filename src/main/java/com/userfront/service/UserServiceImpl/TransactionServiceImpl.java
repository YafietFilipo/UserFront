package com.userfront.service.UserServiceImpl;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.standard.expression.SubtractionExpression;

import com.userfront.dao.PrimaryAccountDao;
import com.userfront.dao.PrimaryTransactionDao;
import com.userfront.dao.RecipientDao;
import com.userfront.dao.SavingsAccountDao;
import com.userfront.dao.SavingsTransactionDao;
import com.userfront.domain.PrimaryAccount;
import com.userfront.domain.PrimaryTransaction;
import com.userfront.domain.Recipient;
import com.userfront.domain.SavingsAccount;
import com.userfront.domain.SavingsTransaction;
import com.userfront.domain.User;
import com.userfront.service.TransactionService;
import com.userfront.service.UserService;

@Service
public class TransactionServiceImpl implements TransactionService {
	@Autowired
	private UserService userService;
	@Autowired
	private PrimaryTransactionDao primaryTransactionDao;
	@Autowired
	private SavingsTransactionDao savingsTransactionDao;

	@Autowired
	private SavingsAccountDao savingsAccountDao;
	@Autowired
	private PrimaryAccountDao primaryAccountDao;
	
	@Autowired
	private RecipientDao recipientDao;
	

	public List<PrimaryTransaction> findPrimaryTransactionList(String username) {
		User user = userService.findByUsername(username);
		List<PrimaryTransaction> primaryTransactionList = user.getPrimaryAccount().getPrimaryTransactionList();

		return primaryTransactionList;

	}

	public List<SavingsTransaction> findSavingsTransactionList(String username) {
		User user = userService.findByUsername(username);
		List<SavingsTransaction> savingsTransactionList = user.getSavingsAccount().getSavingsTransactionList();
		return savingsTransactionList;
	}

	public void savePrimaryDepositTransaction(PrimaryTransaction primaryTransaction) {
		primaryTransactionDao.save(primaryTransaction);
	}

	public void saveSavingsDepositTransaction(SavingsTransaction savingsTransaction) {
		savingsTransactionDao.save(savingsTransaction);
	}

	public void savePrimaryWithdrawTransaction(PrimaryTransaction primaryTransaction) {
		primaryTransactionDao.save(primaryTransaction);
	}

	public void saveSavingsWithdrawTransaction(SavingsTransaction savingsTransaction) {
		savingsTransactionDao.save(savingsTransaction);
	}

	public void betweenAccountsTransfer(String transferFrom, String transferTo, String amount,
			PrimaryAccount primaryAccount, SavingsAccount savingsAccount) throws Exception {
		if (transferFrom.equalsIgnoreCase("Primary") && transferTo.equalsIgnoreCase("Savings")) {
			primaryAccount.setAccountBalance(
					primaryAccount.getAccountBalance().subtract(new BigDecimal(Double.parseDouble(amount))));
			savingsAccount.setAccountBalance(
					savingsAccount.getAccountBalance().add(new BigDecimal(Double.parseDouble(amount))));
			primaryAccountDao.save(primaryAccount);
			savingsAccountDao.save(savingsAccount);

			Date date = new Date();

			PrimaryTransaction primaryTransaction = new PrimaryTransaction(date,
					"Between account transfer " + transferFrom + " to " + transferTo, "Transfer", "Finished",
					Double.parseDouble(amount), primaryAccount.getAccountBalance(), primaryAccount);
			// SavingsTransaction savingsTransaction = new SavingsTransaction(date,"withraw
			// from primary
			// Account","account","Finished",Double.parseDouble(amount),savingsAccount.getAccountBalance(),savingsAccount);
			primaryTransactionDao.save(primaryTransaction);
			// savingsTransactionDao.save(savingsTransaction);

		} else if (transferFrom.equalsIgnoreCase("Savings") && transferTo.equalsIgnoreCase("Primary")) {
			savingsAccount.setAccountBalance(
					savingsAccount.getAccountBalance().subtract(new BigDecimal(Double.parseDouble(amount))));
			primaryAccount.setAccountBalance(
					primaryAccount.getAccountBalance().add(new BigDecimal(Double.parseDouble(amount))));
			savingsAccountDao.save(savingsAccount);
			primaryAccountDao.save(primaryAccount);

			Date date = new Date();
			// PrimaryTransaction primaryTransaction = new
			// PrimaryTransaction(date,"transnaction between Account","account","Finished",
			// Double.parseDouble(amount),
			// primaryAccount.getAccountBalance(),primaryAccount);
			SavingsTransaction savingsTransaction = new SavingsTransaction(date,
					"Between account transfer " + transferFrom + " to " + transferTo,"Transfer ", "Finished",
					Double.parseDouble(amount), savingsAccount.getAccountBalance(), savingsAccount);
			// .primaryTransactionDao.save(primaryTransaction);
			savingsTransactionDao.save(savingsTransaction);

		} else {

			throw new Exception("Invalid Transfer");
		}
	}
	public Recipient findRecipientByName(String recipientName) {
		
		Recipient recipient = recipientDao.findByName(recipientName);
		return recipient;
	
	}
	
	public List<Recipient> findRecipientList(Principal principal){
		String userName = principal.getName();
		//List<Recipient> recipientList = recipientDao.findAll().stream().filter(recipient -> recipient.getUser().equals(user)).collect(Collectors.toList());    
		List<Recipient> recipientList = recipientDao.findAll().stream().filter(recipient -> recipient.getUser().getUsername().equals(userName)).collect(Collectors.toList());                          	
		return recipientList;
	}
	
	public void deleteRecipientByName(String recipientName) {
		//Recipient recipient = recipientDao.findByName(recipientName);
		//recipientDao.deleteByName(recipient.getId());
		recipientDao.deleteByName(recipientName);
	}
	
	public void saveRecipient(Recipient recipient) {
		recipientDao.save(recipient);
	}
	
	public void transferToSomeoneElse(Recipient recipient,String amount,String accountType,PrimaryAccount primaryAccount,SavingsAccount savingsAccount)throws Exception {
		
		if(accountType.equalsIgnoreCase("Primary")) 
		{
			primaryAccount.setAccountBalance(primaryAccount.getAccountBalance().subtract(new BigDecimal(amount)));
			primaryAccountDao.save(primaryAccount);
			
			Date date = new Date();
			
			PrimaryTransaction primaryTransaction = new PrimaryTransaction(date, "Transfer to recipient "+recipient.getName(), "Transfer", "Finished", Double.parseDouble(amount), primaryAccount.getAccountBalance(), primaryAccount);
			primaryTransactionDao.save(primaryTransaction);
			
		}else if(accountType.equalsIgnoreCase("Savings"))
		{
			savingsAccount.setAccountBalance(savingsAccount.getAccountBalance().subtract(new BigDecimal(amount)));
			savingsAccountDao.save(savingsAccount);
			
			Date date = new Date();
			
		SavingsTransaction savingsTransaction = new SavingsTransaction(date, "Transfer to recipient "+recipient.getName(), "Transfer", "Finished", Double.parseDouble(amount), savingsAccount.getAccountBalance(), savingsAccount);
			savingsTransactionDao.save(savingsTransaction);
		}else {
			
			throw new Exception("Invalid Transfer");
		}
		
	}

}
