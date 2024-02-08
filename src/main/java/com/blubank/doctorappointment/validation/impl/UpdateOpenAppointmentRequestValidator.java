package com.blubank.doctorappointment.validation.impl;

import com.blubank.doctorappointment.domain.model.dto.ErrorObjectDto;
import com.blubank.doctorappointment.domain.model.dto.request.UpdateOpenAppointmentRequestDto;
import com.blubank.doctorappointment.validation.ARequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class UpdateOpenAppointmentRequestValidator extends ARequestValidator<UpdateOpenAppointmentRequestDto> {
    private final AppointmentRequestValidator appointmentRequestValidator;

    @Autowired
    public UpdateOpenAppointmentRequestValidator(AppointmentRequestValidator appointmentRequestValidator) {
        this.appointmentRequestValidator = appointmentRequestValidator;
    }

    @Override
    protected boolean validateMandatory(UpdateOpenAppointmentRequestDto request) {
        boolean result = true;
        result = appointmentRequestValidator.validateMandatory(request);
        if (result) {
            if (request.getName() == null) {
                ErrorObjectDto errorObjectDto = new ErrorObjectDto("name", HttpStatus.BAD_REQUEST.value(), MANDATORY_FORMAT_MESSAGE);
                request.getErrorObjects().add(errorObjectDto);
                result = false;
            }
            if (request.getPhoneNumber() == null) {
                ErrorObjectDto errorObjectDto = new ErrorObjectDto("phoneNumber", HttpStatus.BAD_REQUEST.value(), MANDATORY_FORMAT_MESSAGE);
                request.getErrorObjects().add(errorObjectDto);
                result = false;
            }
        }
        return result;
    }

    @Override
    protected boolean validateInternalRequest(UpdateOpenAppointmentRequestDto request) {
        boolean result = true;
        result = appointmentRequestValidator.validateInternalRequest(request);
        if (result) {
            result = validatePhoneNumber(request);
        }
        return result;
    }

    private boolean validatePhoneNumber(UpdateOpenAppointmentRequestDto request) {
        boolean result = true;
        if (!PHONE_NUMBER_PATTERN.matcher(request.getPhoneNumber()).matches()) {
            addErrorObjectToList(request, "phoneNumber", HttpStatus.BAD_REQUEST.value(), INVALID_FORMAT_MESSAGE);
            result = false;
        }
        return result;
    }
}