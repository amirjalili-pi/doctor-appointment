package com.blubank.doctorappointment.validation.impl;

import com.blubank.doctorappointment.domain.model.dto.request.AddOpenAppointmentRequestDto;
import com.blubank.doctorappointment.validation.ARequestValidator;
import org.springframework.stereotype.Component;

@Component
public class AddOpenTimeRequestValidator extends ARequestValidator<AddOpenAppointmentRequestDto> {



    @Override
    public boolean validate(AddOpenAppointmentRequestDto request) {
        return super.validate(request);
    }



}
