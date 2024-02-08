package com.blubank.doctorappointment.validation.impl;

import com.blubank.doctorappointment.domain.model.dto.AAppointmentWsDto;
import com.blubank.doctorappointment.domain.model.dto.ErrorObjectDto;
import com.blubank.doctorappointment.domain.model.dto.request.GetOpenAppointmentRequestDto;
import com.blubank.doctorappointment.validation.ARequestValidator;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class GetOpenAppointmentRequestValidator extends ARequestValidator<GetOpenAppointmentRequestDto> {
    @Override
    protected boolean validateMandatory(GetOpenAppointmentRequestDto request) {
        boolean result = true;
        if (request.getDate() == null) {
            ErrorObjectDto errorObjectDto = new ErrorObjectDto("date", HttpStatus.BAD_REQUEST.value(), MANDATORY_FORMAT_MESSAGE);
            request.getErrorObjects().add(errorObjectDto);
            result = false;
        }
        return result;
    }

    @Override
    protected boolean validateInternalRequest(GetOpenAppointmentRequestDto request) {
        boolean result = true;
        result = validateDate(request);
        return result;
    }

    private boolean validateDate(GetOpenAppointmentRequestDto request) {
        boolean result = true;
        if (!DATE_PATTERN.matcher(request.getDate()).matches()) {
            addErrorObjectToList(request, "date", HttpStatus.BAD_REQUEST.value(), INVALID_FORMAT_MESSAGE);
            result = false;
        }
        return result;
    }
}
