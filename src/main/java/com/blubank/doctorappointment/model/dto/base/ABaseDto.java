package com.blubank.doctorappointment.model.dto.base;

import com.blubank.doctorappointment.model.dto.ErrorObjectDto;

import java.util.ArrayList;
import java.util.List;

public abstract class ABaseDto {
    private List<ErrorObjectDto> errorObjects;

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
