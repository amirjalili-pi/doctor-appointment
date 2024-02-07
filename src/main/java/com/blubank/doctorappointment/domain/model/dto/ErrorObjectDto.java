package com.blubank.doctorappointment.domain.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
//@JsonIgnoreProperties(value = {"httpStatusCode"})
public class ErrorObjectDto {

    private String fieldName;
//    private String httpStatusCode;
    private String message;

}
