package com.blubank.doctorappointment.processor.impl;

import com.blubank.doctorappointment.domain.enumeration.ActionTypeEnum;
import com.blubank.doctorappointment.domain.model.dto.ErrorObjectDto;
import com.blubank.doctorappointment.domain.model.dto.request.RemoveOpenAppointmentRequestDto;
import com.blubank.doctorappointment.domain.model.entity.Appointment;
import com.blubank.doctorappointment.processor.AAppointmentProcessor;
import com.blubank.doctorappointment.service.IAppointmentService;
import com.blubank.doctorappointment.validation.IRequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

@Component
public class RemoveAppointmentProcessorImpl extends AAppointmentProcessor<RemoveOpenAppointmentRequestDto> {
    @Autowired
    protected RemoveAppointmentProcessorImpl(IRequestValidator<RemoveOpenAppointmentRequestDto> requestValidator, IAppointmentService appointmentService) {
        super(requestValidator, appointmentService);
    }

    @Override
    protected boolean executeInternalProcess(RemoveOpenAppointmentRequestDto request) {
        boolean result = true;
        Appointment appointmentReq = mapRequestToAppointment(request);
        LocalTime timeOfStart = appointmentReq.getTimeOfStart();
        LocalTime timeOfFinish = appointmentReq.getTimeOfFinish();
        LocalDate dateOfAppointment = appointmentReq.getDateOfAppointment();
        Optional<Appointment> optionalAppointment = appointmentService.findAppointmentByDateAndTimeOfStartAndTimeOfFinish(dateOfAppointment, timeOfStart, timeOfFinish);
        if (optionalAppointment.isEmpty()) {
            result = false;
            ErrorObjectDto errorObjectDto = new ErrorObjectDto("appointment", HttpStatus.NOT_FOUND.value(), "appointment not found");
            request.getErrorObjects().add(errorObjectDto);
        } else {
            Appointment appointment = optionalAppointment.get();
            if (appointment.getIsReserved()) {
                result = false;
                ErrorObjectDto errorObjectDto = new ErrorObjectDto("appointment", HttpStatus.NOT_ACCEPTABLE.value(), "appointment has been taken");
                request.getErrorObjects().add(errorObjectDto);
            } else {
                appointmentService.deleteAppointment(appointment);
            }
        }
        return result;
    }

    @Override
    public ActionTypeEnum getAction() {
        return ActionTypeEnum.Delete;
    }
}
