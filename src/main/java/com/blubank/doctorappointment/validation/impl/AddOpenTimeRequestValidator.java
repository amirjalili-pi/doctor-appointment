package com.blubank.doctorappointment.validation.impl;

import com.blubank.doctorappointment.domain.model.dto.ErrorObjectDto;
import com.blubank.doctorappointment.domain.model.dto.request.AddOpenTimeRequestDtoRequest;
import com.blubank.doctorappointment.validation.ARequestValidator;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.regex.Pattern;

@Component
public class AddOpenTimeRequestValidator extends ARequestValidator<AddOpenTimeRequestDtoRequest> {

    public static final Pattern DATE_PATTERN = Pattern.compile("(0[1-9]|[12][0-9]|3[01])-(0[1-9]|1[1,2])-(19|20)\\d{2}"); // datePattern
    public static final Pattern TIME_PATTERN = Pattern.compile("([01]?[0-9]|2[0-3]):[0-5][0-9]"); // datePattern
    public static final String INVALID_FORMAT_MESSAGE = "field format is invalid";
    public static final String START_TIME_IS_AFTER_BIGGER_END_TIME_MESSAGE = "timeOfStart is after timeOfFinish";

    @Override
    public boolean validate(AddOpenTimeRequestDtoRequest request) {
        boolean result = true;
        result = validateDate(request);
        result &= validateTimes(request);
        if (result) {
            LocalTime timeOfStart = LocalTime.parse(request.getTimeOfStart());
            LocalTime timeOfFinish = LocalTime.parse(request.getTimeOfFinish());
            if (timeOfStart.isAfter(timeOfFinish)) {
                ErrorObjectDto errorObjectDto = new ErrorObjectDto("timeOfStart-timeOfFinish", START_TIME_IS_AFTER_BIGGER_END_TIME_MESSAGE);
                request.getErrorObjects().add(errorObjectDto);
                result = false;
            }
        }

        return result;
    }

    private boolean validateDate(AddOpenTimeRequestDtoRequest request) {
        boolean result = true;
        if (!DATE_PATTERN.matcher(request.getDate()).matches()) {
            addErrorObjectToList(request, "date", INVALID_FORMAT_MESSAGE);
            result = false;
        }
        return result;
    }

    private boolean validateTimes(AddOpenTimeRequestDtoRequest request) {
        boolean result = true;
        if (!TIME_PATTERN.matcher(request.getTimeOfStart()).matches()) {
            addErrorObjectToList(request, "dateOfStart", INVALID_FORMAT_MESSAGE);
            result = false;
        }
        if (!TIME_PATTERN.matcher(request.getTimeOfFinish()).matches()) {
            addErrorObjectToList(request, "dateOfFinish", INVALID_FORMAT_MESSAGE);
            result = false;
        }
        return result;
    }


}
