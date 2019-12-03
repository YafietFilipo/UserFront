package com.userfront.contoller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.userfront.domain.User;
import com.userfront.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(value="/profile", method=RequestMethod.GET)
	public String profile(Model model,Principal principal) {
		User user = userService.findByUsername(principal.getName());
		model.addAttribute("user", user);
		
		return "profile";
	}
	
	@RequestMapping(value="/profile", method=RequestMethod.POST)
	public String profilePost(@ModelAttribute("user")User newUser, Model model) {
		//User user = new User();
		User user = userService.findByUsername(newUser.getUsername());
		//user.setUserId(newUser.getUserId());//this Creates (entifier of an instance of com.userfront.domain.User was altered from 3 to null) bug because the user's id is retrived from the hidden form attribute
		user.setFirstName(newUser.getFirstName());
		user.setLastName(newUser.getLastName());
		//user.setPassword(newUser.getPassword());//this is a bug
		user.setPhone(newUser.getPhone());
		user.setEmail(newUser.getEmail());
		
		model.addAttribute("user", user);
		userService.saveUser(user);
		
		
		
		return "profile";
	}

}
