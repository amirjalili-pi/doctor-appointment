package com.blubank.doctorappointment.validation.impl;

import com.blubank.doctorappointment.domain.model.dto.AAppointmentWsDto;
import com.blubank.doctorappointment.domain.model.dto.ErrorObjectDto;
import com.blubank.doctorappointment.validation.ARequestValidator;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
public class AppointmentRequestValidator extends ARequestValidator<AAppointmentWsDto> {


    @Override
    public boolean validateInternalRequest(AAppointmentWsDto request) {
        boolean result = true;
        result = validateDate(request);
        result &= validateTimes(request);
        if (result) {
            LocalTime timeOfStart = LocalTime.parse(request.getTimeOfStart());
            LocalTime timeOfFinish = LocalTime.parse(request.getTimeOfFinish());
            if (timeOfStart.isAfter(timeOfFinish)) {
                ErrorObjectDto errorObjectDto = new ErrorObjectDto("timeOfStart-timeOfFinish", HttpStatus.BAD_REQUEST.value(), START_TIME_IS_AFTER_BIGGER_END_TIME_MESSAGE);
                request.getErrorObjects().add(errorObjectDto);
                result = false;
            }
        }
        return result;
    }

    @Override
    public boolean validateMandatory(AAppointmentWsDto request) {
        boolean result = true;
        if (request.getDate() == null) {
            ErrorObjectDto errorObjectDto = new ErrorObjectDto("date", HttpStatus.BAD_REQUEST.value(), MANDATORY_FORMAT_MESSAGE);
            request.getErrorObjects().add(errorObjectDto);
            result = false;
        }
        if (request.getTimeOfStart() == null) {
            ErrorObjectDto errorObjectDto = new ErrorObjectDto("timeOfStart", HttpStatus.BAD_REQUEST.value(), MANDATORY_FORMAT_MESSAGE);
            request.getErrorObjects().add(errorObjectDto);
            result = false;
        }
        if (request.getTimeOfFinish() == null) {
            ErrorObjectDto errorObjectDto = new ErrorObjectDto("timeOfFinish", HttpStatus.BAD_REQUEST.value(), MANDATORY_FORMAT_MESSAGE);
            request.getErrorObjects().add(errorObjectDto);
            result = false;
        }
        return result;
    }


    private boolean validateDate(AAppointmentWsDto request) {
        boolean result = true;
        if (!DATE_PATTERN.matcher(request.getDate()).matches()) {
            addErrorObjectToList(request, "date", HttpStatus.BAD_REQUEST.value(), INVALID_FORMAT_MESSAGE);
            result = false;
        }
        return result;
    }

    private boolean validateTimes(AAppointmentWsDto request) {
        boolean result = true;
        if (!TIME_PATTERN.matcher(request.getTimeOfStart()).matches()) {
            addErrorObjectToList(request, "timeOfStart", HttpStatus.BAD_REQUEST.value(), INVALID_FORMAT_MESSAGE);
            result = false;
        }
        if (!TIME_PATTERN.matcher(request.getTimeOfFinish()).matches()) {
            addErrorObjectToList(request, "timeOfFinish", HttpStatus.BAD_REQUEST.value(), INVALID_FORMAT_MESSAGE);
            result = false;
        }
        return result;
    }

}
