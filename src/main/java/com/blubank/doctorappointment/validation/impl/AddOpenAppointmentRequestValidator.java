package com.blubank.doctorappointment.validation.impl;

import com.blubank.doctorappointment.domain.model.dto.request.AddOpenAppointmentRequestDto;
import com.blubank.doctorappointment.validation.ARequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AddOpenAppointmentRequestValidator extends ARequestValidator<AddOpenAppointmentRequestDto> {


    private final AppointmentRequestValidator appointmentRequestValidator;

    @Autowired
    public AddOpenAppointmentRequestValidator(AppointmentRequestValidator appointmentRequestValidator) {
        this.appointmentRequestValidator = appointmentRequestValidator;
    }

    @Override
    protected boolean validateMandatory(AddOpenAppointmentRequestDto request) {
        return appointmentRequestValidator.validateMandatory(request);
    }

    @Override
    protected boolean validateInternalRequest(AddOpenAppointmentRequestDto request) {
        return appointmentRequestValidator.validateInternalRequest(request);
    }
}
