package com.blubank.doctorappointment.validation.impl;

import com.blubank.doctorappointment.domain.model.dto.ErrorObjectDto;
import com.blubank.doctorappointment.domain.model.dto.request.GetReservedAppointmentsByPhoneRequestDto;
import com.blubank.doctorappointment.validation.ARequestValidator;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class GetReservedAppointmentsByPhoneNumberRequestValidator extends ARequestValidator<GetReservedAppointmentsByPhoneRequestDto> {
    @Override
    protected boolean validateMandatory(GetReservedAppointmentsByPhoneRequestDto request) {
        boolean result = true;
        if (request.getPhoneNumber() == null) {
            ErrorObjectDto errorObjectDto = new ErrorObjectDto("phoneNumber", HttpStatus.BAD_REQUEST.value(), MANDATORY_FORMAT_MESSAGE);
            request.getErrorObjects().add(errorObjectDto);
            result = false;
        }
        return result;
    }

    @Override
    protected boolean validateInternalRequest(GetReservedAppointmentsByPhoneRequestDto request) {
        boolean result = true;
        result = validatePhoneNumber(request);
        return result;
    }

    private boolean validatePhoneNumber(GetReservedAppointmentsByPhoneRequestDto request) {
        boolean result = true;
        if (!PHONE_NUMBER_PATTERN.matcher(request.getPhoneNumber()).matches()) {
            addErrorObjectToList(request, "phoneNumber", HttpStatus.BAD_REQUEST.value(), INVALID_FORMAT_MESSAGE);
            result = false;
        }
        return result;
    }
}
