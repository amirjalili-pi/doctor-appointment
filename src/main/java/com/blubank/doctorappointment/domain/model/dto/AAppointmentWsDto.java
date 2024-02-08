package com.blubank.doctorappointment.domain.model.dto;

import com.blubank.doctorappointment.domain.model.dto.base.ARequestBaseDto;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Setter
@Getter
public abstract class AAppointmentWsDto extends ARequestBaseDto{
    private String date;

    private String timeOfStart;

    private String timeOfFinish;


    @Override
    public String getKey() {
        return getDate() + getTimeOfStart() + getTimeOfFinish();
    }
}
