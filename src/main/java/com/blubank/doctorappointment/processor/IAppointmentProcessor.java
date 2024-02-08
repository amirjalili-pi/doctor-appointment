package com.blubank.doctorappointment.processor;

import com.blubank.doctorappointment.domain.enumeration.ActionTypeEnum;
import com.blubank.doctorappointment.domain.model.dto.AAppointmentWsDto;
import com.blubank.doctorappointment.domain.model.dto.base.ARequestBaseDto;

public interface IAppointmentProcessor<R extends ARequestBaseDto> {
    boolean executeProcess(ActionTypeEnum actionType, R request);

    ActionTypeEnum getAction();
}
