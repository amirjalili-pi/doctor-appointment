package com.blubank.doctorappointment.processor.impl;

import com.blubank.doctorappointment.domain.enumeration.ActionTypeEnum;
import com.blubank.doctorappointment.domain.model.dto.request.AddOpenAppointmentRequestDto;
import com.blubank.doctorappointment.domain.model.entity.Appointment;
import com.blubank.doctorappointment.processor.AAppointmentProcessor;
import com.blubank.doctorappointment.service.IAppointmentService;
import com.blubank.doctorappointment.validation.IRequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Component
public class AddOpenAppointmentProcessorImpl extends AAppointmentProcessor<AddOpenAppointmentRequestDto> {



    @Autowired
    protected AddOpenAppointmentProcessorImpl(IRequestValidator<AddOpenAppointmentRequestDto> requestValidator, IAppointmentService appointmentService) {
        super(requestValidator, appointmentService);
    }


    @Transactional
    @Override
    public boolean executeInternalProcess(AddOpenAppointmentRequestDto request) {
        boolean result = true;
        LocalTime timeOfStart = LocalTime.parse(request.getTimeOfStart());
        LocalTime timeOfEndingAppointment = timeOfStart.plusMinutes(30L);
        LocalTime timeOfFinish = LocalTime.parse(request.getTimeOfFinish());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate dateOfAppointment = LocalDate.parse(request.getDate(), formatter);
        while (timeOfEndingAppointment.isBefore(timeOfFinish)) {

            Appointment appointment = createOpenAppointment(timeOfStart, timeOfEndingAppointment, dateOfAppointment);
            Optional<Appointment> optionalExistAppointment = appointmentService.findAppointmentByDateAndTimeOfStartAndTimeOfFinish(dateOfAppointment, timeOfStart, timeOfEndingAppointment);
            if (optionalExistAppointment.isEmpty()) {
                appointmentService.saveAppointment(appointment);
            }
            timeOfStart = timeOfEndingAppointment;
            timeOfEndingAppointment = timeOfEndingAppointment.plusMinutes(30L);

        }
        return result;
    }

    private Appointment createOpenAppointment(LocalTime timeOfStart, LocalTime timeOfFinish, LocalDate dateOfAppointment) {
        Appointment appointment = new Appointment();
        appointment.setDateOfAppointment(dateOfAppointment);
        appointment.setTimeOfStart(timeOfStart);
        appointment.setTimeOfFinish(timeOfFinish);
        appointment.setIsReserved(Boolean.FALSE);
        return appointment;
    }

    @Override
    public ActionTypeEnum getAction() {
        return ActionTypeEnum.InsertByDoctor;
    }
}
