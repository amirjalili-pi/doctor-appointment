package com.blubank.doctorappointment.service.impl;

import com.blubank.doctorappointment.domain.dao.IAppointmentDao;
import com.blubank.doctorappointment.domain.model.dto.AppointmentInfoWsDto;
import com.blubank.doctorappointment.domain.model.entity.Appointment;
import com.blubank.doctorappointment.service.IAppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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


//    @Transactional
//    @Override
//    public boolean addOpenAppointment(AddAppointmentRequestDtoRequest request) {
//        boolean result = true;
//        result = addOpenTimeRequestValidator.validate(request);
//        if (result) {
//        LocalTime timeOfStart = LocalTime.parse(request.getTimeOfStart());
//        LocalTime timeOfFinish = LocalTime.parse(request.getTimeOfFinish());
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
//        LocalDate dateOfAppointment = LocalDate.parse(request.getDate(), formatter);
//            LocalTime timeOfEndingAppointment = timeOfStart.plusMinutes(30L);
//            while (timeOfEndingAppointment.isBefore(timeOfFinish)) {
//
//                Appointment appointment = createOpenAppointment(timeOfStart, timeOfEndingAppointment, dateOfAppointment);
//                Optional<Appointment> optionalExistAppointment = appointmentRepository.findAppointmentByDateAndStartTimeAndFinishTime(dateOfAppointment, timeOfStart, timeOfEndingAppointment);
//                if (optionalExistAppointment.isEmpty()) {
//                    appointmentRepository.save(appointment);
//                }
//                timeOfStart = timeOfEndingAppointment;
//                timeOfEndingAppointment = timeOfEndingAppointment.plusMinutes(30L);
//
//            }
//        }
//        return result;
//    }

    @Transactional
    @Override
    public void addAppointment(Appointment appointment) {
        appointmentDao.addAppointment(appointment);
    }

    @Transactional
    @Override
    public void deleteAppointment(Appointment appointment) {
        appointmentDao.deleteAppointment(appointment.getDateOfAppointment(), appointment.getTimeOfStart(), appointment.getTimeOfFinish());
    }

    @Transactional
    @Override
    public Optional<Appointment> findAppointmentByDateAndTimeOfStartAndTimeOfFinish(LocalDate dateOfAppointment, LocalTime timeOfStart, LocalTime timeOfFinish) {
        return appointmentDao.findAppointmentByDateAndTimeOfStartAndTimeOfFinish(dateOfAppointment, timeOfStart, timeOfFinish);
    }

    @Transactional
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



    private AppointmentInfoWsDto mapToAppointmentWsDto(Appointment appointment) {
        AppointmentInfoWsDto appointmentWsDto = new AppointmentInfoWsDto();
        appointmentWsDto.setId(appointment.getId());
        appointmentWsDto.setPatientName(appointment.getPatientName());
        appointmentWsDto.setPatientNumber(appointment.getPatientNumber());
        appointmentWsDto.setIsReserved(appointment.getIsReserved());
        appointmentWsDto.setTimeOfStart(appointment.getTimeOfStart().format(DateTimeFormatter.ofPattern("HH:mm")));
        appointmentWsDto.setTimeOfFinish(appointment.getTimeOfFinish().format(DateTimeFormatter.ofPattern("HH:mm")));
        appointmentWsDto.setDateOfAppointment(appointment.getDateOfAppointment().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        return appointmentWsDto;
    }
}
