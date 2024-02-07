package com.blubank.doctorappointment.domain.model.dto.response;

import com.blubank.doctorappointment.domain.model.dto.ErrorObjectDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class ResponseDto {
    private Boolean success;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("errors")
    private List<ErrorObjectDto> errorObjectList = new ArrayList<>();



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
        return errorObjectList;
    }

    public void setErrorObjectList(List<ErrorObjectDto> errorObjectList) {
        this.errorObjectList = errorObjectList;
    }
}
