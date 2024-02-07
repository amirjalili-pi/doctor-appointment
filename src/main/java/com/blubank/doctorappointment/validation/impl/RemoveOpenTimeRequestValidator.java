package com.blubank.doctorappointment.validation.impl;

import com.blubank.doctorappointment.domain.model.dto.request.RemoveOpenAppointmentRequestDto;
import com.blubank.doctorappointment.validation.ARequestValidator;
import org.springframework.stereotype.Component;

@Component
public class RemoveOpenTimeRequestValidator extends ARequestValidator<RemoveOpenAppointmentRequestDto> {
    @Override
    public boolean validate(RemoveOpenAppointmentRequestDto request) {
        return super.validate(request);
    }
}
