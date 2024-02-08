package com.blubank.doctorappointment.domain.model.dto.request;

import com.blubank.doctorappointment.domain.model.dto.base.ARequestBaseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class GetOpenAppointmentsByPhoneRequestDto extends ARequestBaseDto {

    private String phoneNumber;

    @Override
    public String getKey() {
        return phoneNumber;
    }
}
