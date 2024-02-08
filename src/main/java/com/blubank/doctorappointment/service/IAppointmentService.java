package com.blubank.doctorappointment.service;

import com.blubank.doctorappointment.domain.model.dto.AppointmentInfoWsDto;
import com.blubank.doctorappointment.domain.model.entity.Appointment;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface IAppointmentService {
    void addAppointment(Appointment appointment);

    void deleteAppointment(Appointment appointment);

    Optional<Appointment> findAppointmentByDateAndTimeOfStartAndTimeOfFinish(LocalDate dateOfAppointment, LocalTime timeOfStart, LocalTime timeOfFinish);

    List<AppointmentInfoWsDto> getAllAppointmentsAsInfoWsDto();

    List<AppointmentInfoWsDto> findAppointmentsByDateAndReservedFlag(String dateOfAppointment, Boolean isReserved);

//    <R extends AAppointmentWsDto>  boolean startProcess(ActionTypeEnum actionType, R request);


}
