package com.blubank.doctorappointment.processor.impl;

import com.blubank.doctorappointment.domain.enumeration.ActionTypeEnum;
import com.blubank.doctorappointment.domain.model.dto.request.AddOpenAppointmentRequestDto;
import com.blubank.doctorappointment.domain.model.entity.Appointment;
import com.blubank.doctorappointment.domain.model.repository.AppointmentRepository;
import com.blubank.doctorappointment.processor.IAppointmentProcessor;
import com.blubank.doctorappointment.service.IAppointmentService;
import com.blubank.doctorappointment.validation.IRequestValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.thymeleaf.postprocessor.IPostProcessor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

class AddOpenAppointmentProcessorImplTest {

    @Captor
    ArgumentCaptor<Appointment> appointmentArgumentCaptor;
    @Mock
    private IAppointmentService appointmentService;
    @Mock
    private IRequestValidator<AddOpenAppointmentRequestDto> requestValidator;

    @Mock
    private AppointmentRepository appointmentRepository;

    private IAppointmentProcessor<AddOpenAppointmentRequestDto> underTest;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        underTest = new AddOpenAppointmentProcessorImpl(requestValidator, appointmentService);
    }

    @Test
    void itShouldNotSaveAppointmentWhenStartTimeIsAfterFinishTime() {
        //Given
        LocalDate dateOfAppointment = LocalDate.now();
        LocalTime timeOfStart = LocalTime.of(19, 15);
        LocalTime timeOfFinish = LocalTime.of(17, 19);
        Appointment appointment = new Appointment(null, null, null, dateOfAppointment, timeOfStart, timeOfFinish, Boolean.FALSE);
        AddOpenAppointmentRequestDto addOpenAppointmentRequestDto = new AddOpenAppointmentRequestDto();
        addOpenAppointmentRequestDto.setDate(dateOfAppointment.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        addOpenAppointmentRequestDto.setTimeOfStart(timeOfStart.format(DateTimeFormatter.ofPattern("HH:mm")));
        addOpenAppointmentRequestDto.setTimeOfFinish(timeOfFinish.format(DateTimeFormatter.ofPattern("HH:mm")));
        given(requestValidator.validateRequest(addOpenAppointmentRequestDto)).willReturn(Boolean.FALSE);

        //When
        underTest.executeProcess(ActionTypeEnum.InsertByDoctor, addOpenAppointmentRequestDto);
        //Then
        then(appointmentService).should(never()).saveAppointment(appointment);
    }
    @Test
    void itShouldNotSaveAppointmentWhenAppointmentTimeIsLessThanThirtyMinutes() {
        //Given
        LocalDate dateOfAppointment = LocalDate.now();
        LocalTime timeOfStart = LocalTime.of(19, 15);
        LocalTime timeOfFinish = LocalTime.of(19, 30);
        Appointment appointment = new Appointment(null, null, null, dateOfAppointment, timeOfStart, timeOfFinish, Boolean.FALSE);
        AddOpenAppointmentRequestDto addOpenAppointmentRequestDto = new AddOpenAppointmentRequestDto();
        addOpenAppointmentRequestDto.setDate(dateOfAppointment.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        addOpenAppointmentRequestDto.setTimeOfStart(timeOfStart.format(DateTimeFormatter.ofPattern("HH:mm")));
        addOpenAppointmentRequestDto.setTimeOfFinish(timeOfFinish.format(DateTimeFormatter.ofPattern("HH:mm")));
        given(requestValidator.validateRequest(addOpenAppointmentRequestDto)).willReturn(Boolean.FALSE);

        //When
        underTest.executeProcess(ActionTypeEnum.InsertByDoctor, addOpenAppointmentRequestDto);
        //Then
        then(appointmentService).should(never()).saveAppointment(appointment);
    }

    @Test
    void itShouldSaveOpenAppointment() {
        //Given
        LocalDate dateOfAppointment = LocalDate.now();
        LocalTime timeOfStart = LocalTime.of(19, 0);
        LocalTime timeOfFinish = LocalTime.of(19, 30);
        Appointment appointment = new Appointment(null, null, null, dateOfAppointment, timeOfStart, timeOfFinish, Boolean.FALSE);
        AddOpenAppointmentRequestDto addOpenAppointmentRequestDto = new AddOpenAppointmentRequestDto();
        addOpenAppointmentRequestDto.setDate(dateOfAppointment.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        addOpenAppointmentRequestDto.setTimeOfStart(timeOfStart.format(DateTimeFormatter.ofPattern("HH:mm")));
        addOpenAppointmentRequestDto.setTimeOfFinish(timeOfFinish.format(DateTimeFormatter.ofPattern("HH:mm")));
        given(requestValidator.validateRequest(addOpenAppointmentRequestDto)).willReturn(Boolean.TRUE);

        //When
        underTest.executeProcess(ActionTypeEnum.InsertByDoctor, addOpenAppointmentRequestDto);
        //Then
        then(appointmentService).should().saveAppointment(appointmentArgumentCaptor.capture());
        Appointment appointmentArgumentCaptorValue = appointmentArgumentCaptor.getValue();
        assertThat(appointmentArgumentCaptorValue).usingRecursiveComparison()
                .ignoringFields("id", "patientName", "patientPhoneNumber")
                .isEqualTo(appointment);
    }
}