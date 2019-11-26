package com.userfront.contoller;

import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.userfront.domain.Appointment;
import com.userfront.service.AppointmentService;
import com.userfront.service.UserService;

@Controller
@RequestMapping("/appointment")
public class AppointmentController {
	@Autowired
	private UserService userService;
	
	@Autowired
	private AppointmentService appointmentService;
	
	@RequestMapping(value="/create", method=RequestMethod.GET)
	public String appointment(Model model,Principal principal) {
		Appointment appointment = new Appointment();
		model.addAttribute("appointment", appointment);
		model.addAttribute("dateString","");
	
		return "appointment";
		
	}
	
	@RequestMapping(value="/create", method=RequestMethod.POST)
	public String appointmentPost(@ModelAttribute("appointment")Appointment appointment,@ModelAttribute("dateString")String date,Principal principal) throws ParseException {
		
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd hh:mm");
		Date d1 = format1.parse(date);
		appointment.setDate(d1);
		userService.findByUsername(principal.getName());
		
		appointmentService.createAppointment(appointment);
		
		return "redirect:/userFront";
	}

}
