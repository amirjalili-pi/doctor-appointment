package com.blubank.doctorappointment.domain.dao.impl;

import com.blubank.doctorappointment.domain.dao.IAppointmentDao;
import com.blubank.doctorappointment.domain.model.entity.Appointment;
import com.blubank.doctorappointment.domain.model.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Component
public class AppointmentDaoImpl implements IAppointmentDao {

    private final AppointmentRepository appointmentRepository;

    @Autowired
    public AppointmentDaoImpl(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    public Optional<Appointment> findAppointmentByDateAndTimeOfStartAndTimeOfFinish(LocalDate dateOfAppointment, LocalTime timeOfStart, LocalTime timeOfFinish) {
        return appointmentRepository.findAppointmentByDateAndStartTimeAndFinishTime(dateOfAppointment, timeOfStart, timeOfFinish);
    }

    @Override
    public List<Appointment> getAllAppointments() {
        List<Appointment> appointments = appointmentRepository.findAll();
        return appointments;
    }

    @Override
    public void deleteAppointment(Appointment appointment) {
            appointmentRepository.delete(appointment);
    }

    @Override
    public Appointment saveAppointment(Appointment appointment) {
        return appointmentRepository.save(appointment);

    }
    @Override
    public List<Appointment> findAppointmentsByDateAndReservedFlag(LocalDate dateOfAppointment, Boolean isReserved) {
        return appointmentRepository.findAppointmentsByDateAndReservedFlag(dateOfAppointment, isReserved);
    }

    @Override
    public List<Appointment> findAppointmentsByPhoneNumber(String phoneNumber) {
        return appointmentRepository.findAppointmentsByPhoneNumber(phoneNumber);
    }
}
