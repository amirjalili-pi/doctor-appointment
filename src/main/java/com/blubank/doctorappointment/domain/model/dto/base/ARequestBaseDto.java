package com.blubank.doctorappointment.domain.model.dto.base;

import com.blubank.doctorappointment.domain.model.dto.ErrorObjectDto;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

public abstract class ARequestBaseDto {
    @JsonIgnore
    private List<ErrorObjectDto> errorObjects = new ArrayList<>();

    public List<ErrorObjectDto> getErrorObjects() {
        if (errorObjects == null) {
            errorObjects = new ArrayList<>();
        }
        return errorObjects;
    }

    public void setErrorObjects(List<ErrorObjectDto> errorObjects) {
        this.errorObjects = errorObjects;
    }


}
