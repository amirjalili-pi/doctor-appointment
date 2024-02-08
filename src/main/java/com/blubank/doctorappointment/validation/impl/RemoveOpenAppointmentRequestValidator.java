package com.blubank.doctorappointment.validation.impl;

import com.blubank.doctorappointment.domain.model.dto.request.RemoveOpenAppointmentRequestDto;
import com.blubank.doctorappointment.validation.ARequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RemoveOpenAppointmentRequestValidator extends ARequestValidator<RemoveOpenAppointmentRequestDto> {
    private final AppointmentRequestValidator appointmentRequestValidator;

    @Autowired
    public RemoveOpenAppointmentRequestValidator(AppointmentRequestValidator appointmentRequestValidator) {
        this.appointmentRequestValidator = appointmentRequestValidator;
    }

    @Override
    protected boolean validateMandatory(RemoveOpenAppointmentRequestDto request) {
        return appointmentRequestValidator.validateMandatory(request);
    }

    @Override
    protected boolean validateInternalRequest(RemoveOpenAppointmentRequestDto request) {
        return appointmentRequestValidator.validateInternalRequest(request);
    }


}
