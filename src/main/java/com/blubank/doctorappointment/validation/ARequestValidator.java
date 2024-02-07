package com.blubank.doctorappointment.validation;

import com.blubank.doctorappointment.domain.model.dto.AAppointmentWsDto;
import com.blubank.doctorappointment.domain.model.dto.ErrorObjectDto;
import org.springframework.http.HttpStatus;

import java.time.LocalTime;
import java.util.regex.Pattern;

public abstract class ARequestValidator<R extends AAppointmentWsDto> implements IRequestValidator<R>{

    public static final Pattern DATE_PATTERN = Pattern.compile("(0[1-9]|[12][0-9]|3[01])-(0[1-9]|1[1,2])-(19|20)\\d{2}"); // datePattern
    public static final Pattern TIME_PATTERN = Pattern.compile("([01]?[0-9]|2[0-3]):[0-5][0-9]"); // datePattern
    public static final String INVALID_FORMAT_MESSAGE = "field format is invalid";
    public static final String MANDATORY_FORMAT_MESSAGE = "field is mandatory";
    public static final String START_TIME_IS_AFTER_BIGGER_END_TIME_MESSAGE = "timeOfStart is after timeOfFinish";

    @Override
    public boolean validate(R request) {
        boolean result = true;
        result = validateMandatory(request);
        if (result) {
            result = validateDate(request);
            result &= validateTimes(request);
        }
        if (result) {
            LocalTime timeOfStart = LocalTime.parse(request.getTimeOfStart());
            LocalTime timeOfFinish = LocalTime.parse(request.getTimeOfFinish());
            if (timeOfStart.isAfter(timeOfFinish)) {
                ErrorObjectDto errorObjectDto = new ErrorObjectDto("timeOfStart-timeOfFinish", HttpStatus.BAD_REQUEST.value(),START_TIME_IS_AFTER_BIGGER_END_TIME_MESSAGE);
                request.getErrorObjects().add(errorObjectDto);
                result = false;
            }
        }

        return result;
    }

    private boolean validateMandatory(R request) {
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

    public void addErrorObjectToList(R request, String field, Integer httpStatus ,String message) {
        ErrorObjectDto errorObjectDto = new ErrorObjectDto(field, httpStatus,message);
        request.getErrorObjects().add(errorObjectDto);
    }

    private boolean validateDate(R request) {
        boolean result = true;
        if (!DATE_PATTERN.matcher(request.getDate()).matches()) {
            addErrorObjectToList(request, "date", HttpStatus.BAD_REQUEST.value(),INVALID_FORMAT_MESSAGE);
            result = false;
        }
        return result;
    }

    private boolean validateTimes(R request) {
        boolean result = true;
        if (!TIME_PATTERN.matcher(request.getTimeOfStart()).matches()) {
            addErrorObjectToList(request, "dateOfStart", HttpStatus.BAD_REQUEST.value(), INVALID_FORMAT_MESSAGE);
            result = false;
        }
        if (!TIME_PATTERN.matcher(request.getTimeOfFinish()).matches()) {
            addErrorObjectToList(request, "dateOfFinish", HttpStatus.BAD_REQUEST.value(),INVALID_FORMAT_MESSAGE);
            result = false;
        }
        return result;
    }


}
