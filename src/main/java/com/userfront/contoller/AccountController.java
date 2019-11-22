package com.userfront.contoller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.userfront.domain.PrimaryAccount;
import com.userfront.domain.PrimaryTransaction;
import com.userfront.domain.SavingsAccount;
import com.userfront.domain.SavingsTransaction;
import com.userfront.domain.User;
import com.userfront.service.AccountService;
import com.userfront.service.UserService;

@Controller
@RequestMapping("/account")
public class AccountController {

	@Autowired
	private UserService userService;

	@Autowired
	private AccountService accountService;

	@RequestMapping("/primaryAccount")
	public String primaryAccount(Principal principal, Model model) {
		User user = userService.findByUsername(principal.getName());
		PrimaryAccount primaryAccount = user.getPrimaryAccount();
		model.addAttribute("primaryAccount", primaryAccount);
		List<PrimaryTransaction> primaryTransactionList = user.getPrimaryAccount().getPrimaryTransactionList();
		model.addAttribute("primaryTransactionList", primaryTransactionList);

		return "primaryAccount";
	}

	@RequestMapping("/savingsAccount")
	public String savingsAccount(Principal principal, Model model) {
		User user = userService.findByUsername(principal.getName());
		SavingsAccount savingsAccount = user.getSavingsAccount();
		model.addAttribute("savingsAccount", savingsAccount);
		List<SavingsTransaction> savingsTransactionList = user.getSavingsAccount().getSavingsTransactionList();
		model.addAttribute("savingsTransactionList", savingsTransactionList);
		return "savingsAccount";
	}

	@RequestMapping(value = "/deposit", method = RequestMethod.GET)
	public String deposit(Model model) {
		model.addAttribute("amount", "");
		model.addAttribute("accountType", "");
		return "deposit";
	}

	@RequestMapping(value = "/deposit", method = RequestMethod.POST)
	public String depositPost(@ModelAttribute("amount") String amount,
			@ModelAttribute("accountType") String accountType, Principal principal, Model model) {
		accountService.deposit(accountType, Double.parseDouble(amount), principal);

		return "redirect:/userFront";
	}

	@RequestMapping(value = "/withdraw", method = RequestMethod.GET)
	public String withdraw(Model model) {
		model.addAttribute("amount", "");
		model.addAttribute("accountType", "");
		return "withdraw";
	}

	@RequestMapping(value = "/withdraw", method = RequestMethod.POST)
	public String withdrawPost(@ModelAttribute("amount") String amount,
			@ModelAttribute("accountType") String accountType, Principal principal, Model model) {
		accountService.withdraw(accountType, Double.parseDouble(amount), principal);

		return "redirect:/userFront";
	}

}
