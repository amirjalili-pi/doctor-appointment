package com.blubank.doctorappointment.service.impl;

import com.blubank.doctorappointment.domain.dao.IAppointmentDao;
import com.blubank.doctorappointment.domain.model.dto.AppointmentInfoWsDto;
import com.blubank.doctorappointment.domain.model.entity.Appointment;
import com.blubank.doctorappointment.service.IAppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AppointmentServiceImpl implements IAppointmentService {

    private final IAppointmentDao appointmentDao;

    @Autowired
    public AppointmentServiceImpl(IAppointmentDao appointmentDao) {
        this.appointmentDao = appointmentDao;
    }

    @Override
    public Appointment saveAppointment(Appointment appointment) {
        return appointmentDao.saveAppointment(appointment);
    }




    @Override
    public void deleteAppointment(Appointment appointment) {
        appointmentDao.deleteAppointment(appointment.getDateOfAppointment(), appointment.getTimeOfStart(), appointment.getTimeOfFinish());
    }

    @Override
    public Optional<Appointment> findAppointmentByDateAndTimeOfStartAndTimeOfFinish(LocalDate dateOfAppointment, LocalTime timeOfStart, LocalTime timeOfFinish) {
        return appointmentDao.findAppointmentByDateAndTimeOfStartAndTimeOfFinish(dateOfAppointment, timeOfStart, timeOfFinish);
    }

    @Override
    public List<AppointmentInfoWsDto> getAllAppointmentsAsInfoWsDto() {
        List<Appointment> appointments = appointmentDao.getAllAppointments();
        List<AppointmentInfoWsDto> appointmentWsDtoList = new ArrayList<>();
        if (appointments.isEmpty()) {
            return appointmentWsDtoList;
        }
        appointmentWsDtoList = appointments.stream().map(this::mapToAppointmentWsDto).collect(Collectors.toList());
        return appointmentWsDtoList;
    }

    @Override
    public List<AppointmentInfoWsDto> findAppointmentsByDateAndReservedFlag(String date, Boolean isReserved) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate dateOfAppointment = LocalDate.parse(date, formatter);
        List<Appointment> appointments = appointmentDao.findAppointmentsByDateAndReservedFlag(dateOfAppointment, isReserved);
        List<AppointmentInfoWsDto> appointmentInfoWsDtoList = appointments.stream().map(this::mapToAppointmentWsDto).collect(Collectors.toList());
        return appointmentInfoWsDtoList;
    }

    @Override
    public List<AppointmentInfoWsDto> findAppointmentsByPhoneNumber(String phoneNumber) {
        List<Appointment> appointments = appointmentDao.findAppointmentsByPhoneNumber(phoneNumber);
        List<AppointmentInfoWsDto> appointmentInfoWsDtoList = appointments.stream().map(this::mapToAppointmentWsDto).collect(Collectors.toList());
        return appointmentInfoWsDtoList;
    }


    private AppointmentInfoWsDto mapToAppointmentWsDto(Appointment appointment) {
        AppointmentInfoWsDto appointmentWsDto = new AppointmentInfoWsDto();
        appointmentWsDto.setId(appointment.getId());
        appointmentWsDto.setPatientName(appointment.getPatientName());
        appointmentWsDto.setPatientPhoneNumber(appointment.getPatientPhoneNumber());
        appointmentWsDto.setIsReserved(appointment.getIsReserved());
        appointmentWsDto.setTimeOfStart(appointment.getTimeOfStart().format(DateTimeFormatter.ofPattern("HH:mm")));
        appointmentWsDto.setTimeOfFinish(appointment.getTimeOfFinish().format(DateTimeFormatter.ofPattern("HH:mm")));
        appointmentWsDto.setDateOfAppointment(appointment.getDateOfAppointment().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        return appointmentWsDto;
    }
}
