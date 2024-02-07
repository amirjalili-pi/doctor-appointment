package com.blubank.doctorappointment.controller;

import com.blubank.doctorappointment.domain.enumeration.ActionTypeEnum;
import com.blubank.doctorappointment.domain.model.dto.AppointmentInfoWsDto;
import com.blubank.doctorappointment.domain.model.dto.ErrorObjectDto;
import com.blubank.doctorappointment.domain.model.dto.base.ARequestBaseDto;
import com.blubank.doctorappointment.domain.model.dto.request.AddOpenAppointmentRequestDto;
import com.blubank.doctorappointment.domain.model.dto.request.RemoveOpenAppointmentRequestDto;
import com.blubank.doctorappointment.domain.model.dto.response.ResponseDto;
import com.blubank.doctorappointment.factory.AppointmentProcessorFactory;
import com.blubank.doctorappointment.processor.IAppointmentProcessor;
import com.blubank.doctorappointment.service.IAppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/v1/appointment")
public class AppointmentController {

    private final AppointmentProcessorFactory appointmentProcessorFactory;

    private final IAppointmentService appointmentService;

    @Autowired
    public AppointmentController(AppointmentProcessorFactory appointmentProcessorFactory, IAppointmentService appointmentService) {
        this.appointmentProcessorFactory = appointmentProcessorFactory;
        this.appointmentService = appointmentService;
    }

    @DeleteMapping("/doctor/delete-open-appointment")
    public ResponseEntity<ResponseDto> deleteOpenTime(@Valid @RequestBody RemoveOpenAppointmentRequestDto requestDto) {
        try {
            IAppointmentProcessor appointmentProcessor = appointmentProcessorFactory.getAppointmentProcessor(ActionTypeEnum.Delete);
            boolean result = appointmentProcessor.executeProcess(ActionTypeEnum.Delete, requestDto);
            if (!result) {
                ResponseDto responseDto = handleErrorMessages(requestDto, Boolean.FALSE);
                return new ResponseEntity<>(responseDto, HttpStatus.resolve(responseDto.getHttpStatus()));
            }
            ResponseDto responseDto = handleErrorMessages(requestDto, Boolean.TRUE);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        } catch (Exception e) {
            ResponseDto responseDto = new ResponseDto(Boolean.FALSE, null);
            return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/doctor/add-open_appointment")
    public ResponseEntity<ResponseDto> addOpenTime(@Valid @RequestBody AddOpenAppointmentRequestDto requestDto) {
        try {
            IAppointmentProcessor appointmentProcessor = appointmentProcessorFactory.getAppointmentProcessor(ActionTypeEnum.Insert);
            boolean result = appointmentProcessor.executeProcess(ActionTypeEnum.Insert, requestDto);
            if (!result) {
                ResponseDto responseDto = handleErrorMessages(requestDto, Boolean.FALSE);
                return new ResponseEntity<>(responseDto, HttpStatus.resolve(responseDto.getHttpStatus()));
            }
            ResponseDto responseDto = handleErrorMessages(requestDto, Boolean.TRUE);
            return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
        } catch (Exception e) {
            // add log
            ResponseDto responseDto = new ResponseDto(Boolean.FALSE, null);
            return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    @GetMapping("/doctor/all-appointments")
    public ResponseEntity<List<AppointmentInfoWsDto>> getAllAppointments() {
        try {
            List<AppointmentInfoWsDto> allAppointments = appointmentService.getAllAppointmentsAsInfoWsDto();
            return new ResponseEntity<>(allAppointments, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private <R extends ARequestBaseDto> ResponseDto handleErrorMessages(R request, boolean result) {

        ResponseDto responseDto = new ResponseDto();
        if (!result) {
            responseDto.setSuccess(Boolean.FALSE);
            responseDto.getErrorObjectList().addAll(request.getErrorObjects());
            Optional<ErrorObjectDto> objectDto = request.getErrorObjects().stream().findAny();
            if (objectDto.isPresent()) {
                responseDto.setHttpStatus(objectDto.get().getHttpStatusCode());
            } else {
                responseDto.setHttpStatus(HttpStatus.BAD_REQUEST.value());
            }
        } else {
            responseDto.setSuccess(Boolean.TRUE);
            responseDto.setErrorObjectList(null);
        }
        return responseDto;
    }
}
