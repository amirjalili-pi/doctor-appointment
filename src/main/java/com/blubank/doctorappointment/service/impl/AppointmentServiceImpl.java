package com.blubank.doctorappointment.service.impl;

import com.blubank.doctorappointment.domain.model.dto.AppointmentWsDto;
import com.blubank.doctorappointment.domain.model.dto.request.AddOpenTimeRequestDtoRequest;
import com.blubank.doctorappointment.domain.model.entity.Appointment;
import com.blubank.doctorappointment.domain.model.repository.AppointmentRepository;
import com.blubank.doctorappointment.service.IAppointmentService;
import com.blubank.doctorappointment.validation.impl.AddOpenTimeRequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppointmentServiceImpl implements IAppointmentService {

    private final AppointmentRepository appointmentRepository;

    private final AddOpenTimeRequestValidator addOpenTimeRequestValidator;

    @Autowired
    public AppointmentServiceImpl(AppointmentRepository appointmentRepository, AddOpenTimeRequestValidator addOpenTimeRequestValidator) {
        this.appointmentRepository = appointmentRepository;
        this.addOpenTimeRequestValidator = addOpenTimeRequestValidator;
    }
    @Transactional
    @Override
    public boolean addOpenAppointment(AddOpenTimeRequestDtoRequest request) {
        boolean result = true;
        result = addOpenTimeRequestValidator.validate(request);
        if (result) {
        LocalTime timeOfStart = LocalTime.parse(request.getTimeOfStart());
        LocalTime timeOfFinish = LocalTime.parse(request.getTimeOfFinish());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate dateOfAppointment = LocalDate.parse(request.getDate(), formatter);
            LocalTime timeOfEndingAppointment = timeOfStart.plusMinutes(30L);
            while (timeOfEndingAppointment.isBefore(timeOfFinish)) {

                Appointment appointment = createOpenAppointment(timeOfStart, timeOfEndingAppointment, dateOfAppointment);
                appointmentRepository.save(appointment);
                timeOfStart = timeOfEndingAppointment;
                timeOfEndingAppointment = timeOfEndingAppointment.plusMinutes(30L);

            }
        }
        return result;
    }

    @Override
    public List<AppointmentWsDto> getAllAppointments() {
        List<Appointment> appointments = appointmentRepository.findAll();
        List<AppointmentWsDto> appointmentWsDtoList = new ArrayList<>();
        if (appointments.isEmpty()) {
            return appointmentWsDtoList;
        }
        appointmentWsDtoList = appointments.stream().map(this::mapToAppointmentWsDto).collect(Collectors.toList());
        return appointmentWsDtoList;
    }

    private Appointment createOpenAppointment(LocalTime timeOfStart, LocalTime timeOfFinish, LocalDate dateOfAppointment) {
        Appointment appointment = new Appointment();
        appointment.setDateOfAppointment(dateOfAppointment);
        appointment.setTimeOfStart(timeOfStart);
        appointment.setTimeOfFinish(timeOfFinish);
        appointment.setIsReserved(Boolean.FALSE);
        return appointment;
    }

    private AppointmentWsDto mapToAppointmentWsDto(Appointment appointment) {
        AppointmentWsDto appointmentWsDto = new AppointmentWsDto();
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
