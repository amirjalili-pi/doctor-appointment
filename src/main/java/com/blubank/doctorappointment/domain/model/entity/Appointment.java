package com.blubank.doctorappointment.domain.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column
    private String patientName;
    @Column
    private String patientNumber;
    @Column(nullable = false)
    private LocalDate dateOfAppointment;
    @Column(nullable = false)
    private LocalTime timeOfStart;
    @Column(nullable = false)
    private LocalTime timeOfFinish;
    @Column(nullable = false)
    private Boolean isReserved;

}