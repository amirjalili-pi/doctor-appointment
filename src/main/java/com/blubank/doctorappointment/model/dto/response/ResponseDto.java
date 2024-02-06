package com.blubank.doctorappointment.model.dto.response;

import com.blubank.doctorappointment.model.dto.ErrorObjectDto;

import java.util.ArrayList;
import java.util.List;

public class ResponseDto {
    private Boolean success;

    private List<ErrorObjectDto> errorObjectList;


    public ResponseDto(Boolean success, List<ErrorObjectDto> errorObjectList) {
        this.success = success;
        this.errorObjectList = errorObjectList;
    }

    public ResponseDto() {
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<ErrorObjectDto> getErrorObjectList() {
        if (errorObjectList == null) {
            errorObjectList = new ArrayList<>();
        }
        return errorObjectList;
    }

    public void setErrorObjectList(List<ErrorObjectDto> errorObjectList) {
        this.errorObjectList = errorObjectList;
    }
}
