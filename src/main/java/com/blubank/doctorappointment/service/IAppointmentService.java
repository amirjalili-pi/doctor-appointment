package com.blubank.doctorappointment.service;

import com.blubank.doctorappointment.domain.model.dto.AppointmentWsDto;
import com.blubank.doctorappointment.domain.model.dto.request.AddOpenTimeRequestDtoRequest;

import java.util.List;

public interface IAppointmentService {
    boolean addOpenAppointment(AddOpenTimeRequestDtoRequest request);

    List<AppointmentWsDto> getAllAppointments();
}
