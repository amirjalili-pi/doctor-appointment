package com.blubank.doctorappointment.processor;

import com.blubank.doctorappointment.domain.enumeration.ActionTypeEnum;
import com.blubank.doctorappointment.domain.model.dto.AAppointmentWsDto;

public interface IAppointmentProcessor<R extends AAppointmentWsDto> {
    boolean executeProcess(ActionTypeEnum actionType, R request);

    ActionTypeEnum getAction();
}
