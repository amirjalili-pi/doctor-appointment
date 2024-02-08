package com.blubank.doctorappointment.domain.model.repository;

import com.blubank.doctorappointment.domain.model.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {


    @Query(value = "select * from appointment where date_of_appointment = :date_of_appointment and time_of_start = :time_of_start and time_of_finish = :time_of_finish", nativeQuery = true)
    Optional<Appointment> findAppointmentByDateAndStartTimeAndFinishTime(@Param("date_of_appointment") LocalDate dateOfAppointment,@Param("time_of_start") LocalTime timeOfStart,@Param("time_of_finish") LocalTime timeOfFinish);

    @Query(value = "select * from appointment where date_of_appointment = :date_of_appointment and is_reserved = :is_reserved", nativeQuery = true)
    List<Appointment> findAppointmentsByDateAndReservedFlag(@Param("date_of_appointment") LocalDate dateOfAppointment, @Param("is_reserved") Boolean isReserved);

    @Query(value = "select * from appointment where patient_phone_number = :patient_phone_number", nativeQuery = true)
    List<Appointment> findAppointmentsByPhoneNumber(@Param("patient_phone_number") String phoneNumber);
}
