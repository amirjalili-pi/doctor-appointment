package com.blubank.doctorappointment.processor.impl;

import com.blubank.doctorappointment.domain.enumeration.ActionTypeEnum;
import com.blubank.doctorappointment.domain.model.dto.request.AddOpenAppointmentRequestDto;
import com.blubank.doctorappointment.domain.model.dto.request.RemoveOpenAppointmentRequestDto;
import com.blubank.doctorappointment.domain.model.entity.Appointment;
import com.blubank.doctorappointment.processor.IAppointmentProcessor;
import com.blubank.doctorappointment.service.IAppointmentService;
import com.blubank.doctorappointment.validation.IRequestValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

class RemoveAppointmentProcessorImplTest {
    @Mock
    private IAppointmentService appointmentService;
    @Mock
    private IRequestValidator<RemoveOpenAppointmentRequestDto> requestValidator;

    private IAppointmentProcessor<RemoveOpenAppointmentRequestDto> underTest;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        underTest = new RemoveAppointmentProcessorImpl(requestValidator, appointmentService);
    }


    @Test
    void itShouldNotDeleteWhenNoAppointmentAvailable() {
        //Given
        LocalDate dateOfAppointment = LocalDate.now();
        LocalTime timeOfStart = LocalTime.of(19, 15);
        LocalTime timeOfFinish = LocalTime.of(20, 30);
        Appointment appointment = new Appointment(null, null, null, dateOfAppointment, timeOfStart, timeOfFinish, Boolean.FALSE);
        RemoveOpenAppointmentRequestDto removeOpenAppointmentRequestDto = new RemoveOpenAppointmentRequestDto();
        removeOpenAppointmentRequestDto.setDate(dateOfAppointment.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        removeOpenAppointmentRequestDto.setTimeOfStart(timeOfStart.format(DateTimeFormatter.ofPattern("HH:mm")));
        removeOpenAppointmentRequestDto.setTimeOfFinish(timeOfFinish.format(DateTimeFormatter.ofPattern("HH:mm")));
        given(requestValidator.validateRequest(removeOpenAppointmentRequestDto)).willReturn(Boolean.TRUE);
        //When
        underTest.executeProcess(ActionTypeEnum.DeleteByDoctor, removeOpenAppointmentRequestDto);
        //Then
        assertThat(removeOpenAppointmentRequestDto.getErrorObjects().get(0).getHttpStatusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
        then(appointmentService).should(never()).deleteAppointment(appointment);

    }

    @Test
    void itShouldNotDeleteWhenAppointmentTaken() {
        //Given
        LocalDate dateOfAppointment = LocalDate.now();
        LocalTime timeOfStart = LocalTime.of(19, 15);
        LocalTime timeOfFinish = LocalTime.of(20, 30);
        Appointment appointment = new Appointment(null, "amir", "09391123939", dateOfAppointment, timeOfStart, timeOfFinish, Boolean.TRUE);
        RemoveOpenAppointmentRequestDto removeOpenAppointmentRequestDto = new RemoveOpenAppointmentRequestDto();
        removeOpenAppointmentRequestDto.setDate(dateOfAppointment.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        removeOpenAppointmentRequestDto.setTimeOfStart(timeOfStart.format(DateTimeFormatter.ofPattern("HH:mm")));
        removeOpenAppointmentRequestDto.setTimeOfFinish(timeOfFinish.format(DateTimeFormatter.ofPattern("HH:mm")));
        given(requestValidator.validateRequest(removeOpenAppointmentRequestDto)).willReturn(Boolean.TRUE);
        given(appointmentService.findAppointmentByDateAndTimeOfStartAndTimeOfFinish(dateOfAppointment, timeOfStart, timeOfFinish)).willReturn(Optional.of(appointment));
        //When
        underTest.executeProcess(ActionTypeEnum.DeleteByDoctor, removeOpenAppointmentRequestDto);
        //Then
        assertThat(removeOpenAppointmentRequestDto.getErrorObjects().get(0).getHttpStatusCode()).isEqualTo(HttpStatus.NOT_ACCEPTABLE.value());
        then(appointmentService).should(never()).deleteAppointment(appointment);

    }
}