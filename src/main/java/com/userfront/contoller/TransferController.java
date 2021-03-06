package com.userfront.contoller;

import java.security.Principal;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.userfront.domain.PrimaryAccount;
import com.userfront.domain.Recipient;
import com.userfront.domain.SavingsAccount;
import com.userfront.domain.User;
import com.userfront.service.TransactionService;
import com.userfront.service.UserService;

@Controller
@RequestMapping("/transfer")
public class TransferController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private TransactionService transactionService; 
	
	@RequestMapping(value="/betweenAccounts", method=RequestMethod.GET)
	public String betweenAccouts(Model model) {
		
		model.addAttribute("transferFrom", "");
		model.addAttribute("tranferTo", "");
		model.addAttribute("amount", "");
		
		return "/betweenAccounts";
		
	}
	
	@RequestMapping(value="/betweenAccounts", method=RequestMethod.POST)
	public String betweenAccountsPost(@ModelAttribute("transferFrom") String transferFrom,
			@ModelAttribute("transferTo") String transferTo,
			@ModelAttribute("amount")String amount,Principal principal)throws Exception {
		User user = userService.findByUsername(principal.getName());
		PrimaryAccount primaryAccount = user.getPrimaryAccount();
		SavingsAccount savingsAccount = user.getSavingsAccount();
		
		transactionService.betweenAccountsTransfer(transferFrom,transferTo,amount,primaryAccount,savingsAccount);
		return "redirect:/userFront";
		
	}
	
	
	@RequestMapping(value="/recipient", method=RequestMethod.GET)
	public String recipient(Model model,Principal principal) {
		
		Recipient recipient = new Recipient();
		
		List<Recipient> recipientList = transactionService.findRecipientList(principal);
		model.addAttribute("recipient", recipient);
		model.addAttribute("recipientList", recipientList);
		return "recipient";
	}
	
	@RequestMapping(value = "/recipient/save" , method = RequestMethod.POST)
	public String recipientSave(@ModelAttribute("recipient") Recipient recipient,Principal principal) {
		User user = userService.findByUsername(principal.getName());
		recipient.setUser(user);
		transactionService.saveRecipient(recipient);

		return "redirect:/transfer/recipient";
	}
	@RequestMapping(value="/recipient/edit", method = RequestMethod.GET)
	public String recipientEdit(@RequestParam(value="recipientName") String recipientName,Model model,Principal principal) {
		
		Recipient recipient = transactionService.findRecipientByName(recipientName);
		List<Recipient> recipientList = transactionService.findRecipientList(principal);
		
		
		model.addAttribute("recipient",recipient);
		model.addAttribute("recipientList", recipientList);
		
		return "recipient";
	}
	@RequestMapping(value="/recipient/delete", method=RequestMethod.GET)
	@Transactional
	public String recipientDelete(@RequestParam(value="recipientName")String recipientName,Model model,Principal principal) {
		
		transactionService.deleteRecipientByName(recipientName);
		Recipient recipient = new Recipient();
		List<Recipient> recipientList = transactionService.findRecipientList(principal);
		model.addAttribute("recipientList", recipientList);
		model.addAttribute("recipient", recipient);
		return "recipient";
	}
	
	
	@RequestMapping(value="/toSomeoneElse" , method = RequestMethod.GET)
	public String toSomeoneElse(Model model, Principal principal) {
		
		List<Recipient> recipientList = transactionService.findRecipientList(principal);
		model.addAttribute("recipientList", recipientList);
		model.addAttribute("accountType", "");
		
		
		return "toSomeoneElse";
	}
	
	@RequestMapping(value="/toSomeoneElse" , method = RequestMethod.POST)
	public String toSomeoneElsePost(@ModelAttribute(value="amount")String amount,
			@ModelAttribute("accountType")String accountType,Principal principal,
			@ModelAttribute(value="recipientName")String recipientName)throws Exception {
		
		
		Recipient recipient = transactionService.findRecipientByName(recipientName);
		User user = userService.findByUsername(principal.getName());
		PrimaryAccount primaryAccount = user.getPrimaryAccount();
		SavingsAccount savingsAccount = user.getSavingsAccount();
		
		transactionService.transferToSomeoneElse(recipient,amount,accountType,primaryAccount,savingsAccount);
		
		return "redirect:/userFront";
	}
	

}
