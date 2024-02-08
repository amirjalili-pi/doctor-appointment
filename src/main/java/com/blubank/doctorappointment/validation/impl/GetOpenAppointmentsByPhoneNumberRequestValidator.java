package com.blubank.doctorappointment.validation.impl;

import com.blubank.doctorappointment.domain.model.dto.ErrorObjectDto;
import com.blubank.doctorappointment.domain.model.dto.request.GetOpenAppointmentsByPhoneRequestDto;
import com.blubank.doctorappointment.validation.ARequestValidator;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class GetOpenAppointmentsByPhoneNumberRequestValidator extends ARequestValidator<GetOpenAppointmentsByPhoneRequestDto> {
    @Override
    protected boolean validateMandatory(GetOpenAppointmentsByPhoneRequestDto request) {
        boolean result = true;
        if (request.getPhoneNumber() == null) {
            ErrorObjectDto errorObjectDto = new ErrorObjectDto("phoneNumber", HttpStatus.BAD_REQUEST.value(), MANDATORY_FORMAT_MESSAGE);
            request.getErrorObjects().add(errorObjectDto);
            result = false;
        }
        return result;
    }

    @Override
    protected boolean validateInternalRequest(GetOpenAppointmentsByPhoneRequestDto request) {
        boolean result = true;
        result = validatePhoneNumber(request);
        return result;
    }

    private boolean validatePhoneNumber(GetOpenAppointmentsByPhoneRequestDto request) {
        boolean result = true;
        if (!PHONE_NUMBER_PATTERN.matcher(request.getPhoneNumber()).matches()) {
            addErrorObjectToList(request, "phoneNumber", HttpStatus.BAD_REQUEST.value(), INVALID_FORMAT_MESSAGE);
            result = false;
        }
        return result;
    }
}
