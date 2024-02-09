package com.blubank.doctorappointment.processor.impl;

import com.blubank.doctorappointment.domain.enumeration.ActionTypeEnum;
import com.blubank.doctorappointment.domain.model.dto.ErrorObjectDto;
import com.blubank.doctorappointment.domain.model.dto.request.UpdateOpenAppointmentRequestDto;
import com.blubank.doctorappointment.domain.model.entity.Appointment;
import com.blubank.doctorappointment.processor.AAppointmentProcessor;
import com.blubank.doctorappointment.service.IAppointmentService;
import com.blubank.doctorappointment.validation.IRequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Component
public class UpdateOpenAppointmentProcessorImpl extends AAppointmentProcessor<UpdateOpenAppointmentRequestDto> {
    @Autowired
    protected UpdateOpenAppointmentProcessorImpl(IRequestValidator<UpdateOpenAppointmentRequestDto> requestValidator, IAppointmentService appointmentService) {
        super(requestValidator, appointmentService);
    }

    @Transactional
    @Override
    public boolean executeInternalProcess(UpdateOpenAppointmentRequestDto request) {
        boolean result = true;
        LocalTime timeOfStart = LocalTime.parse(request.getTimeOfStart());
        LocalTime timeOfFinish = LocalTime.parse(request.getTimeOfFinish());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate dateOfAppointment = LocalDate.parse(request.getDate(), formatter);
        Optional<Appointment> optionalAppointment = appointmentService.findAppointmentByDateAndTimeOfStartAndTimeOfFinish(dateOfAppointment, timeOfStart, timeOfFinish);
        if (optionalAppointment.isEmpty()) {
            result = false;
            ErrorObjectDto errorObjectDto = new ErrorObjectDto("appointment", HttpStatus.NOT_FOUND.value(), APPOINTMENT_NOT_FOUND_MESSAGE);
            request.getErrorObjects().add(errorObjectDto);
        } else {
            Appointment appointment = optionalAppointment.get();
            if (appointment.getIsReserved()) {
                result = false;
                ErrorObjectDto errorObjectDto = new ErrorObjectDto("appointment", HttpStatus.NOT_ACCEPTABLE.value(), APPOINTMENT_HAS_BEEN_TAKEN_MESSAGE);
                request.getErrorObjects().add(errorObjectDto);
            } else {
                appointment.setPatientName(request.getName());
                appointment.setPatientPhoneNumber(request.getPhoneNumber());
                appointment.setIsReserved(Boolean.TRUE);
                appointmentService.saveAppointment(appointment);
            }
        }
        return result;
    }

    @Override
    public ActionTypeEnum getAction() {
        return ActionTypeEnum.UpdateByPatient;
    }
}
