package com.blubank.doctorappointment.domain.dao;

import com.blubank.doctorappointment.domain.model.dto.AppointmentInfoWsDto;
import com.blubank.doctorappointment.domain.model.entity.Appointment;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface IAppointmentDao {

    Optional<Appointment> findAppointmentByDateAndTimeOfStartAndTimeOfFinish(LocalDate dateOfAppointment, LocalTime timeOfStart, LocalTime timeOfFinish);

    List<Appointment> getAllAppointments();

    boolean deleteAppointment(LocalDate dateOfAppointment, LocalTime timeOfStart, LocalTime timeOfFinish);

    boolean addAppointment(Appointment appointment);

    List<Appointment> findAppointmentByDateAndReservedFlag(LocalDate dateOfAppointment, Boolean isReserved);

}
