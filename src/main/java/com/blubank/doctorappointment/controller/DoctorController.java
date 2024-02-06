package com.blubank.doctorappointment.controller;

import com.blubank.doctorappointment.model.dto.request.AddOpenTimeRequestDto;
import com.blubank.doctorappointment.model.dto.response.ResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalTime;


@RestController
@RequestMapping("/api/v1/doctor")
public class DoctorController {

    @PostMapping
    public ResponseEntity<ResponseDto> addOpenTime(@RequestBody AddOpenTimeRequestDto requestDto) {
        return null;
    }
}
