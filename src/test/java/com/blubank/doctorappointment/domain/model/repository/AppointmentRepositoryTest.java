package com.blubank.doctorappointment.domain.model.repository;

import com.blubank.doctorappointment.domain.model.entity.Appointment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest(
        properties = "spring.jpa.properties.javax.persistence.validation.mode=none"
)
class AppointmentRepositoryTest {

    @Autowired
    private AppointmentRepository underTest;


    @Test
    void itShouldFindAppointmentByDateAndTimeOfStartAndTimeOfFinish() {
        //Given
        LocalDate dateOfAppointment = LocalDate.now();
        LocalTime timeOfStart = LocalTime.of(15, 15);
        LocalTime timeOfFinish = LocalTime.of(17, 19);
        Appointment appointment = new Appointment(null, null, null, dateOfAppointment, timeOfStart, timeOfFinish, Boolean.FALSE);
        //When
        underTest.save(appointment);
        //Then

        assertThat(underTest.findAppointmentByDateAndStartTimeAndFinishTime(dateOfAppointment, timeOfStart, timeOfFinish))
                .isPresent()
                .hasValueSatisfying(a -> {
                    assertThat(a.getDateOfAppointment()).isEqualTo(dateOfAppointment);
                    assertThat(a.getTimeOfStart()).isEqualTo(timeOfStart);
                    assertThat(a.getTimeOfFinish()).isEqualTo(timeOfFinish);
                });
    }

    @Test
    void itShouldFindAppointmentByDateAndReservedFlag() {
        //Given
        LocalDate dateOfAppointment = LocalDate.now();
        LocalTime timeOfStart = LocalTime.of(15, 15);
        LocalTime timeOfFinish = LocalTime.of(17, 19);
        boolean reservedFlag = false;
        Appointment appointment = new Appointment(null, null, null, dateOfAppointment, timeOfStart, timeOfFinish, reservedFlag);
        //When
        underTest.save(appointment);
        //Then
        assertThat(underTest.findAppointmentsByDateAndReservedFlag(dateOfAppointment, reservedFlag))
                .hasSizeGreaterThan(0)
                .hasAtLeastOneElementOfType(Appointment.class)
                .contains(appointment);
    }


    @Test
    void itShouldFindAppointmentByPhoneNumber() {
        //Given
        String name = "amirmahdi";
        String phoneNumber = "09391113939";
        LocalDate dateOfAppointment = LocalDate.now();
        LocalTime timeOfStart = LocalTime.of(15, 15);
        LocalTime timeOfFinish = LocalTime.of(17, 19);
        boolean reservedFlag = false;
        Appointment appointment = new Appointment(null, name, phoneNumber, dateOfAppointment, timeOfStart, timeOfFinish, reservedFlag);
        //When
        underTest.save(appointment);
        //Then
        assertThat(underTest.findAppointmentsByPhoneNumber(phoneNumber))
                .hasSizeGreaterThan(0)
                .hasAtLeastOneElementOfType(Appointment.class)
                .contains(appointment);
    }
}