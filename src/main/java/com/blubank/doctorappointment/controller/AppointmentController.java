package com.blubank.doctorappointment.controller;

import com.blubank.doctorappointment.domain.model.dto.AppointmentWsDto;
import com.blubank.doctorappointment.domain.model.dto.base.ARequestBaseDto;
import com.blubank.doctorappointment.domain.model.dto.request.AddOpenTimeRequestDtoRequest;
import com.blubank.doctorappointment.domain.model.dto.response.ResponseDto;
import com.blubank.doctorappointment.service.IAppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/api/v1/appointment")
public class AppointmentController {

    private final IAppointmentService appointmentService;

    @Autowired
    public AppointmentController(IAppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping("/doctor/add-open_appointment")
    public ResponseEntity<ResponseDto> addOpenTime(@RequestBody @Valid AddOpenTimeRequestDtoRequest requestDto) {
        try {
            boolean result = appointmentService.addOpenAppointment(requestDto);
            if (!result) {
                ResponseDto responseDto = handleErrorMessages(requestDto, Boolean.FALSE);
                return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
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
    public ResponseEntity<List<AppointmentWsDto>> getAllAppointments() {
        try {
            List<AppointmentWsDto> allAppointments = appointmentService.getAllAppointments();
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
        } else {
            responseDto.setSuccess(Boolean.TRUE);
            responseDto.setErrorObjectList(null);
        }
        return responseDto;
    }
}
