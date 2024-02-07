package com.blubank.doctorappointment.domain.model.dto.request;

import com.blubank.doctorappointment.domain.model.dto.AAppointmentWsDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data

public class RemoveOpenAppointmentRequestDto extends AAppointmentWsDto {
}
