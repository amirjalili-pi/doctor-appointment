package com.blubank.doctorappointment.processor.impl;

import com.blubank.doctorappointment.domain.enumeration.ActionTypeEnum;
import com.blubank.doctorappointment.domain.model.dto.request.RemoveOpenAppointmentRequestDto;
import com.blubank.doctorappointment.domain.model.dto.request.UpdateOpenAppointmentRequestDto;
import com.blubank.doctorappointment.domain.model.entity.Appointment;
import com.blubank.doctorappointment.processor.IAppointmentProcessor;
import com.blubank.doctorappointment.service.IAppointmentService;
import com.blubank.doctorappointment.validation.IRequestValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
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

class UpdateOpenAppointmentProcessorImplTest {

    @Captor
    private ArgumentCaptor<Appointment> appointmentArgumentCaptor;
    @Mock
    private IAppointmentService appointmentService;
    @Mock
    private IRequestValidator<UpdateOpenAppointmentRequestDto> requestValidator;

    private IAppointmentProcessor<UpdateOpenAppointmentRequestDto> underTest;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        underTest = new UpdateOpenAppointmentProcessorImpl(requestValidator, appointmentService);
    }

    @Test
    void itShouldNotUpdateAppointmentWhenAppointmentNotFound() {
        //Given
        LocalDate dateOfAppointment = LocalDate.now();
        LocalTime timeOfStart = LocalTime.of(19, 15);
        LocalTime timeOfFinish = LocalTime.of(20, 30);
        Appointment appointment = new Appointment(null, null, null, dateOfAppointment, timeOfStart, timeOfFinish, Boolean.FALSE);
        UpdateOpenAppointmentRequestDto updateOpenAppointmentRequestDto = new UpdateOpenAppointmentRequestDto();
        updateOpenAppointmentRequestDto.setPhoneNumber("09391123939");
        updateOpenAppointmentRequestDto.setName("amir");
        updateOpenAppointmentRequestDto.setDate(dateOfAppointment.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        updateOpenAppointmentRequestDto.setTimeOfStart(timeOfStart.format(DateTimeFormatter.ofPattern("HH:mm")));
        updateOpenAppointmentRequestDto.setTimeOfFinish(timeOfFinish.format(DateTimeFormatter.ofPattern("HH:mm")));
        given(appointmentService.findAppointmentByDateAndTimeOfStartAndTimeOfFinish(dateOfAppointment, timeOfStart, timeOfFinish)).willReturn(Optional.empty());
        given(requestValidator.validateRequest(updateOpenAppointmentRequestDto)).willReturn(Boolean.TRUE);
        //When
        underTest.executeProcess(ActionTypeEnum.UpdateByPatient, updateOpenAppointmentRequestDto);
        //Then
        assertThat(updateOpenAppointmentRequestDto.getErrorObjects().get(0).getHttpStatusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
        then(appointmentService).should(never()).saveAppointment(appointment);
    }

    @Test
    void itShouldNotUpdateAppointmentWhenAppointmentIsTaken() {
        //Given
        LocalDate dateOfAppointment = LocalDate.now();
        LocalTime timeOfStart = LocalTime.of(19, 15);
        LocalTime timeOfFinish = LocalTime.of(20, 30);
        String phoneNumber = "09391123939";
        String name = "amir";
        Appointment appointment = new Appointment(1L, name, phoneNumber, dateOfAppointment, timeOfStart, timeOfFinish, Boolean.TRUE);
        UpdateOpenAppointmentRequestDto updateOpenAppointmentRequestDto = new UpdateOpenAppointmentRequestDto();
        updateOpenAppointmentRequestDto.setPhoneNumber(phoneNumber);
        updateOpenAppointmentRequestDto.setName(name);
        updateOpenAppointmentRequestDto.setDate(dateOfAppointment.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        updateOpenAppointmentRequestDto.setTimeOfStart(timeOfStart.format(DateTimeFormatter.ofPattern("HH:mm")));
        updateOpenAppointmentRequestDto.setTimeOfFinish(timeOfFinish.format(DateTimeFormatter.ofPattern("HH:mm")));
        given(appointmentService.findAppointmentByDateAndTimeOfStartAndTimeOfFinish(dateOfAppointment, timeOfStart, timeOfFinish)).willReturn(Optional.of(appointment));
        given(requestValidator.validateRequest(updateOpenAppointmentRequestDto)).willReturn(Boolean.TRUE);
        //When
        underTest.executeProcess(ActionTypeEnum.UpdateByPatient, updateOpenAppointmentRequestDto);
        //Then

        assertThat(updateOpenAppointmentRequestDto.getErrorObjects().get(0).getHttpStatusCode()).isEqualTo(HttpStatus.NOT_ACCEPTABLE.value());
        then(appointmentService).should(never()).saveAppointment(appointment);
    }

    @Test
    void itShouldUpdateAppointmentWhenAppointmentIsOpen() {
        //Given
        LocalDate dateOfAppointment = LocalDate.now();
        LocalTime timeOfStart = LocalTime.of(19, 15);
        LocalTime timeOfFinish = LocalTime.of(20, 30);
        String phoneNumber = "09391123939";
        String name = "amir";
        long id = 1L;
        Appointment appointment = new Appointment(null, name, phoneNumber, dateOfAppointment, timeOfStart, timeOfFinish, Boolean.TRUE);
        Appointment openAppointment = new Appointment(id, null, null, dateOfAppointment, timeOfStart, timeOfFinish, Boolean.FALSE);
        UpdateOpenAppointmentRequestDto updateOpenAppointmentRequestDto = new UpdateOpenAppointmentRequestDto();
        updateOpenAppointmentRequestDto.setPhoneNumber(phoneNumber);
        updateOpenAppointmentRequestDto.setName(name);
        updateOpenAppointmentRequestDto.setDate(dateOfAppointment.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        updateOpenAppointmentRequestDto.setTimeOfStart(timeOfStart.format(DateTimeFormatter.ofPattern("HH:mm")));
        updateOpenAppointmentRequestDto.setTimeOfFinish(timeOfFinish.format(DateTimeFormatter.ofPattern("HH:mm")));
        given(appointmentService.findAppointmentByDateAndTimeOfStartAndTimeOfFinish(dateOfAppointment, timeOfStart, timeOfFinish)).willReturn(Optional.of(openAppointment));
        given(requestValidator.validateRequest(updateOpenAppointmentRequestDto)).willReturn(Boolean.TRUE);
        //When
        underTest.executeProcess(ActionTypeEnum.UpdateByPatient, updateOpenAppointmentRequestDto);
        //Then
        then(appointmentService).should().saveAppointment(appointmentArgumentCaptor.capture());
        Appointment appointmentArgumentCaptorValue = appointmentArgumentCaptor.getValue();
        assertThat(appointmentArgumentCaptorValue).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(appointment);
    }
}