package com.blubank.doctorappointment.domain.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentInfoWsDto {
    @JsonIgnore
    private Long id;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String patientName;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String patientPhoneNumber;
    private String dateOfAppointment;
    private String timeOfStart;
    private String timeOfFinish;
    private Boolean isReserved;

}
