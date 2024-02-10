package com.blubank.doctorappointment.domain.model.dto.request;

import com.blubank.doctorappointment.domain.model.dto.base.ARequestBaseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetReservedAppointmentsByPhoneRequestDto extends ARequestBaseDto {

    private String phoneNumber;

    @Override
    public String getKey() {
        return phoneNumber;
    }
}
