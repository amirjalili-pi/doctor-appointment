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
    public boolean deleteAppointment(LocalDate dateOfAppointment, LocalTime timeOfStart, LocalTime timeOfFinish) {
        Optional<Appointment> optionalAppointment = appointmentRepository.findAppointmentByDateAndStartTimeAndFinishTime(dateOfAppointment, timeOfStart, timeOfFinish);
        if (optionalAppointment.isPresent()) {
            appointmentRepository.delete(optionalAppointment.get());
            return true;
        }
        return false;
    }

    @Override
    public boolean addAppointment(Appointment appointment) {
        appointmentRepository.save(appointment);
        return true;
    }

    @Override
    public List<Appointment> findAppointmentByDateAndReservedFlag(LocalDate dateOfAppointment, Boolean isReserved) {
        return appointmentRepository.findAppointmentsByDateAndReservedFlag(dateOfAppointment, isReserved);
    }
}
