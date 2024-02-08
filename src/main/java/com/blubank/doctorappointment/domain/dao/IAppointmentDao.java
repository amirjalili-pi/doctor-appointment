package com.blubank.doctorappointment.domain.dao;

import com.blubank.doctorappointment.domain.model.entity.Appointment;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface IAppointmentDao {

    Optional<Appointment> findAppointmentByDateAndTimeOfStartAndTimeOfFinish(LocalDate dateOfAppointment, LocalTime timeOfStart, LocalTime timeOfFinish);

    List<Appointment> getAllAppointments();

    void deleteAppointment(LocalDate dateOfAppointment, LocalTime timeOfStart, LocalTime timeOfFinish);

    void saveAppointment(Appointment appointment);
    List<Appointment> findAppointmentsByDateAndReservedFlag(LocalDate dateOfAppointment, Boolean isReserved);

    List<Appointment> findAppointmentsByPhoneNumber(String phoneNumber);

}
