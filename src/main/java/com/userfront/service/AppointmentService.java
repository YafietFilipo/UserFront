package com.userfront.service;

import java.util.List;

import com.userfront.domain.Appointment;

public interface AppointmentService {
	
	Appointment findAppointment(Long id);
	List<Appointment> findAll();
	void confirmAppointment(Long id);
	Appointment createAppointment(Appointment appointment);

}
