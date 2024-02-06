package com.blubank.doctorappointment.model.dto.request;

import com.blubank.doctorappointment.model.dto.base.ABaseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalTime;


@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddOpenTimeRequestDto extends ABaseDto implements Serializable {
    private String date;
    private String timeOfStart;
    private String timeOfFinish;

}
