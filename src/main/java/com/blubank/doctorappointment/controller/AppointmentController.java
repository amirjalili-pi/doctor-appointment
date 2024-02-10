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
import com.blubank.doctorappointment.validation.impl.GetReservedAppointmentsByPhoneNumberRequestValidator;
import com.blubank.doctorappointment.validation.impl.GetOpenAppointmentsRequestValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/v1/appointment")
@Tag(name = "AppointmentApi", description = "patient can reserve, open appointment and see their reserved appointments, doctors must be authenticated, they can add, remove open appointments and see open, taken appointments")
@Slf4j
public class AppointmentController {

    private final AppointmentProcessorFactory appointmentProcessorFactory;

    private final GetOpenAppointmentsRequestValidator getOpenAppointmentsRequestValidator;

    private final GetReservedAppointmentsByPhoneNumberRequestValidator getReservedAppointmentsByPhoneNumberRequestValidator;

    private final IAppointmentService appointmentService;

    @Autowired
    public AppointmentController(AppointmentProcessorFactory appointmentProcessorFactory, GetOpenAppointmentsRequestValidator getOpenAppointmentsRequestValidator, GetReservedAppointmentsByPhoneNumberRequestValidator getReservedAppointmentsByPhoneNumberRequestValidator, IAppointmentService appointmentService) {
        this.appointmentProcessorFactory = appointmentProcessorFactory;
        this.getOpenAppointmentsRequestValidator = getOpenAppointmentsRequestValidator;
        this.getReservedAppointmentsByPhoneNumberRequestValidator = getReservedAppointmentsByPhoneNumberRequestValidator;
        this.appointmentService = appointmentService;
    }

    @DeleteMapping("/doctor/delete-open-appointment")
    @Operation(summary = "doctors can delete open appointments", description = "must be authenticated")
    public ResponseEntity<ResponseDto> deleteOpenAppointment(@Valid @RequestBody RemoveOpenAppointmentRequestDto requestDto) {
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
            log.debug("an exception occurred during delete open appointment: " + e.getMessage());
            ResponseDto responseDto = new ResponseDto(Boolean.FALSE, null);
            return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/doctor/add-open-appointment")
    @Operation(summary = "doctors can add open appointment", description = "must be authenticated")
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
            log.debug("an exception occurred during adding open appointment: " + e.getMessage());
            ResponseDto responseDto = new ResponseDto(Boolean.FALSE, null);
            return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    @GetMapping("/doctor/appointments")
    @Operation(summary = "doctors can see all appointments: open, taken", description = "must be authenticated")
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
            log.debug("an exception occurred during get all appointmetn: " + e.getMessage());
            ResponseDto responseDto = new ResponseDto(Boolean.FALSE, null);
            return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/patient/open-appointments/{date}")
    @Operation(summary = "patient can see open appointment by entering date", description = "all users permitted")
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
            log.debug("an exception occurred during getting appointment by patient: " + e.getMessage());
            ResponseDto responseDto = new ResponseDto(Boolean.FALSE, null);
            return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/patient/patient-appointments/{phoneNumber}")
    @Operation(summary = "patient can see their reserved appointment by entering phoneNumber", description = "all users permitted")
    public ResponseEntity<ResponseDto> getOpenAppointmentsByPhoneNumber(@PathVariable("phoneNumber") String phoneNumber) {
        try {
            GetReservedAppointmentsByPhoneRequestDto requestDto = new GetReservedAppointmentsByPhoneRequestDto(phoneNumber);
            boolean result = getReservedAppointmentsByPhoneNumberRequestValidator.validateRequest(requestDto);
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
            log.debug("an exception occurred during fetching reserved appointments by phoneNumber: " + e.getMessage());
            ResponseDto responseDto = new ResponseDto(Boolean.FALSE, null);
            return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/patient/update-open-appointment")
    @Operation(summary = "patient can see reserve appointment", description = "all users permitted")
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
            log.debug("an excpetion occurred during updating open appointment by patient: " + e.getMessage());
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
