package com.blubank.doctorappointment.validation;

import com.blubank.doctorappointment.domain.model.dto.ErrorObjectDto;
import com.blubank.doctorappointment.domain.model.dto.base.ARequestBaseDto;

public abstract class ARequestValidator<R extends ARequestBaseDto> implements IRequestValidator<R>{

    public void addErrorObjectToList(R request, String field, String message) {
        ErrorObjectDto errorObjectDto = new ErrorObjectDto(field, message);
        request.getErrorObjects().add(errorObjectDto);
    }

}
