package com.userfront.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.userfront.domain.Appointment;
import com.userfront.service.AppointmentService;

@RestController
@RequestMapping("api/appointment")
public class AppointmentResource {
	
	@Autowired
	private AppointmentService appointmentService;
	
	@RequestMapping("/all")
	public List<Appointment> findAppointmentList()
	{
		List<Appointment> appointmentList = appointmentService.findAll();
		return appointmentList;
	}
	
	@RequestMapping("{appointmentId}/confirm")
	public void confirmAppointment(@PathVariable("id") Long appointmentId)
	{
		appointmentService.confirmAppointment(appointmentId);
	}

}
