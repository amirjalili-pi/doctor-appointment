package com.blubank.doctorappointment.domain.model.repository;

import com.blubank.doctorappointment.domain.model.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    @Query(value = "select * from appointment where date_of_appointment = :date_of_appointment", nativeQuery = true)
    List<Appointment> findAppointmentByDateOfCreate(@Param("date_of_appointment") LocalDate dateOfAppointment);
}
