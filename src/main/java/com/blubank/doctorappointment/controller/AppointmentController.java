package com.blubank.doctorappointment.controller;

import com.blubank.doctorappointment.domain.enumeration.ActionTypeEnum;
import com.blubank.doctorappointment.domain.model.dto.AppointmentInfoWsDto;
import com.blubank.doctorappointment.domain.model.dto.ErrorObjectDto;
import com.blubank.doctorappointment.domain.model.dto.base.ARequestBaseDto;
import com.blubank.doctorappointment.domain.model.dto.request.*;
import com.blubank.doctorappointment.domain.model.dto.response.ResponseDto;
import com.blubank.doctorappointment.factory.AppointmentProcessorFactory;
import com.blubank.doctorappointment.processor.IAppointmentProcessor;
import com.blubank.doctorappointment.service.IAppointmentService;
import com.blubank.doctorappointment.validation.impl.GetOpenAppointmentsByPhoneNumberRequestValidator;
import com.blubank.doctorappointment.validation.impl.GetOpenAppointmentsRequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/v1/appointment")
public class AppointmentController {

    private final AppointmentProcessorFactory appointmentProcessorFactory;

    private final GetOpenAppointmentsRequestValidator getOpenAppointmentsRequestValidator;

    private final GetOpenAppointmentsByPhoneNumberRequestValidator getOpenAppointmentsByPhoneNumberRequestValidator;

    private final IAppointmentService appointmentService;

    @Autowired
    public AppointmentController(AppointmentProcessorFactory appointmentProcessorFactory, GetOpenAppointmentsRequestValidator getOpenAppointmentsRequestValidator, GetOpenAppointmentsByPhoneNumberRequestValidator getOpenAppointmentsByPhoneNumberRequestValidator, IAppointmentService appointmentService) {
        this.appointmentProcessorFactory = appointmentProcessorFactory;
        this.getOpenAppointmentsRequestValidator = getOpenAppointmentsRequestValidator;
        this.getOpenAppointmentsByPhoneNumberRequestValidator = getOpenAppointmentsByPhoneNumberRequestValidator;
        this.appointmentService = appointmentService;
    }

    @DeleteMapping("/doctor/delete-open-appointment")
    public ResponseEntity<ResponseDto> deleteOpenTime(@Valid @RequestBody RemoveOpenAppointmentRequestDto requestDto) {
        try {
            IAppointmentProcessor appointmentProcessor = appointmentProcessorFactory.getAppointmentProcessor(ActionTypeEnum.DeleteByDoctor);
            boolean result = appointmentProcessor.executeProcess(ActionTypeEnum.DeleteByDoctor, requestDto);
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

    @PostMapping("/doctor/add-open-appointment")
    public ResponseEntity<ResponseDto> addOpenAppointment(@Valid @RequestBody AddOpenAppointmentRequestDto requestDto) {
        try {
            IAppointmentProcessor appointmentProcessor = appointmentProcessorFactory.getAppointmentProcessor(ActionTypeEnum.InsertByDoctor);
            boolean result = appointmentProcessor.executeProcess(ActionTypeEnum.InsertByDoctor, requestDto);
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
    @GetMapping("/doctor/appointments")
    public ResponseEntity<ResponseDto> getAllAppointments() {
        try {
            List<AppointmentInfoWsDto> allAppointments = appointmentService.getAllAppointmentsAsInfoWsDto();
            ResponseDto responseDto = new ResponseDto();
            responseDto.setAppointmentInfoWsDtoList(allAppointments);
            responseDto.setSuccess(true);
            responseDto.setHttpStatus(HttpStatus.OK.value());
            responseDto.setErrorObjectList(null);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        } catch (Exception e) {
            ResponseDto responseDto = new ResponseDto(Boolean.FALSE, null);
            return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/patient/open-appointments/{date}")
    public ResponseEntity<ResponseDto> getOpenAppointmentByDate(@PathVariable("date") String dateOfAppointment) {
        try {
            GetOpenAppointmentsRequestDto requestDto = new GetOpenAppointmentsRequestDto(dateOfAppointment);
            boolean result = getOpenAppointmentsRequestValidator.validateRequest(requestDto);
            if (!result) {
                ResponseDto responseDto = handleErrorMessages(requestDto, result);
                return new ResponseEntity<>(responseDto, HttpStatus.resolve(responseDto.getHttpStatus()));
            } else {
                List<AppointmentInfoWsDto> appointmentInfoWsDtos = appointmentService
                        .findAppointmentsByDateAndReservedFlag(requestDto.getDate(), Boolean.FALSE);
                ResponseDto responseDto = new ResponseDto();
                responseDto.setAppointmentInfoWsDtoList(appointmentInfoWsDtos);
                responseDto.setSuccess(true);
                responseDto.setHttpStatus(HttpStatus.OK.value());
                responseDto.setErrorObjectList(null);
                return new ResponseEntity<>(responseDto, HttpStatus.OK);

            }
        } catch (Exception e) {
            ResponseDto responseDto = new ResponseDto(Boolean.FALSE, null);
            return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/patient/patient-appointments/{phoneNumber}")
    public ResponseEntity<ResponseDto> getOpenAppointmentsByPhoneNumber(@PathVariable("phoneNumber") String phoneNumber) {
        try {
            GetOpenAppointmentsByPhoneRequestDto requestDto = new GetOpenAppointmentsByPhoneRequestDto(phoneNumber);
            boolean result = getOpenAppointmentsByPhoneNumberRequestValidator.validateRequest(requestDto);
            if (!result) {
                ResponseDto responseDto = handleErrorMessages(requestDto, result);
                return new ResponseEntity<>(responseDto, HttpStatus.resolve(responseDto.getHttpStatus()));
            } else {
                List<AppointmentInfoWsDto> appointmentInfoWsDtoList = appointmentService
                        .findAppointmentsByPhoneNumber(requestDto.getPhoneNumber());
                ResponseDto responseDto = new ResponseDto();
                responseDto.setAppointmentInfoWsDtoList(appointmentInfoWsDtoList);
                responseDto.setSuccess(true);
                responseDto.setHttpStatus(HttpStatus.OK.value());
                responseDto.setErrorObjectList(null);
                return new ResponseEntity<>(responseDto, HttpStatus.OK);

            }
        } catch (Exception e) {
            ResponseDto responseDto = new ResponseDto(Boolean.FALSE, null);
            return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/patient/update-open-appointment")
    public ResponseEntity<ResponseDto> updateOpenAppointment(@Valid @RequestBody UpdateOpenAppointmentRequestDto requestDto) {
        try {
            IAppointmentProcessor appointmentProcessor = appointmentProcessorFactory.getAppointmentProcessor(ActionTypeEnum.UpdateByPatient);
            boolean result = appointmentProcessor.executeProcess(ActionTypeEnum.UpdateByPatient, requestDto);
            if (!result) {
                ResponseDto responseDto = handleErrorMessages(requestDto, Boolean.FALSE);
                return new ResponseEntity<>(responseDto, HttpStatus.resolve(responseDto.getHttpStatus()));
            }
            ResponseDto responseDto = handleErrorMessages(requestDto, Boolean.TRUE);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        } catch (Exception e) {
            // add log
            ResponseDto responseDto = new ResponseDto(Boolean.FALSE, null);
            return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
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
