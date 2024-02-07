package com.blubank.doctorappointment.processor;

import com.blubank.doctorappointment.domain.enumeration.ActionTypeEnum;
import com.blubank.doctorappointment.domain.model.dto.AAppointmentWsDto;
import com.blubank.doctorappointment.domain.model.dto.ErrorObjectDto;
import com.blubank.doctorappointment.domain.model.entity.Appointment;
import com.blubank.doctorappointment.service.IAppointmentService;
import com.blubank.doctorappointment.validation.IRequestValidator;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Optional;

public abstract class AAppointmentProcessor<R extends AAppointmentWsDto> implements IAppointmentProcessor<R> {


    HazelcastInstance hzInstance = Hazelcast.newHazelcastInstance();
    private final IRequestValidator<R> requestValidator;

    protected final IAppointmentService appointmentService;


    protected AAppointmentProcessor(IRequestValidator<R> requestValidator, IAppointmentService appointmentService) {
        this.requestValidator = requestValidator;
        this.appointmentService = appointmentService;
    }

    @Override
    public boolean executeProcess(ActionTypeEnum actionType, R request) {
        Map<String, String> keyMap = hzInstance.getMap("data");

        try {
            boolean result = true;
            result = requestValidator.validate(request);
            if (result) {
                if (actionType == ActionTypeEnum.Insert) {
                    result = executeInternalProcess(request);
                } else {
                    String key = request.getDate() + request.getTimeOfStart() + request.getTimeOfFinish();
                        if (keyMap.containsKey(key)) {
                            result = false;
                            ErrorObjectDto errorObjectDto = new ErrorObjectDto("process: " + actionType.toString() + "Appointment", HttpStatus.REQUEST_TIMEOUT.value(), "same request is in process please try again a few seconds later");
                            request.getErrorObjects().add(errorObjectDto);
                        } else {
                            keyMap.put(key, "appointment");
                        }
                        result = executeInternalProcess(request);


                }
            }
            return result;
        } catch (Exception e) {
            ErrorObjectDto errorObjectDto = new ErrorObjectDto("process: " + actionType.toString() + "Appointment", HttpStatus.INTERNAL_SERVER_ERROR.value(), "an exception occurred during executing process");
            request.getErrorObjects().add(errorObjectDto);
            return false;
        } finally {
            keyMap.clear();
        }

    }

    protected Appointment mapRequestToAppointment(R request) {
        Appointment appointment = new Appointment();
        LocalTime timeOfStart = LocalTime.parse(request.getTimeOfStart());
        LocalTime timeOfFinish = LocalTime.parse(request.getTimeOfFinish());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate dateOfAppointment = LocalDate.parse(request.getDate(), formatter);
        appointment.setTimeOfStart(timeOfStart);
        appointment.setTimeOfFinish(timeOfFinish);
        appointment.setDateOfAppointment(dateOfAppointment);
        return appointment;
    }

    protected abstract boolean executeInternalProcess(R request);
}