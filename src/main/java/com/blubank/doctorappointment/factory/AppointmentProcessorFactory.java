package com.blubank.doctorappointment.factory;

import com.blubank.doctorappointment.domain.enumeration.ActionTypeEnum;
import com.blubank.doctorappointment.domain.model.dto.AAppointmentWsDto;
import com.blubank.doctorappointment.processor.IAppointmentProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AppointmentProcessorFactory<R extends AAppointmentWsDto> {

    private final List<IAppointmentProcessor<R>> appointmentProcessorList;

    private final Map<ActionTypeEnum, IAppointmentProcessor<R>> appointmentProcessorMap = new HashMap<>();

    @Autowired
    public AppointmentProcessorFactory(List<IAppointmentProcessor<R>> appointmentProcessorList) {
        this.appointmentProcessorList = appointmentProcessorList;
    }

    @PostConstruct
    private void init() {
        for (IAppointmentProcessor<R> appointmentProcessor : appointmentProcessorList) {
            appointmentProcessorMap.put(appointmentProcessor.getAction(), appointmentProcessor);
        }
    }

    public IAppointmentProcessor<R> getAppointmentProcessor(ActionTypeEnum actionType) {
        return appointmentProcessorMap.get(actionType);
    }
}
