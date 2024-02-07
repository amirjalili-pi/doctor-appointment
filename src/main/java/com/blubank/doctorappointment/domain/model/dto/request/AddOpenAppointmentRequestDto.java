package com.blubank.doctorappointment.domain.model.dto.request;

import com.blubank.doctorappointment.domain.model.dto.AAppointmentWsDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;


@EqualsAndHashCode(callSuper = true)
@Data

public class AddOpenAppointmentRequestDto extends AAppointmentWsDto implements Serializable {

}
