package com.blubank.doctorappointment.domain.model.dto.request;

import com.blubank.doctorappointment.domain.model.dto.base.ARequestBaseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;


@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddOpenTimeRequestDtoRequest extends ARequestBaseDto implements Serializable {
    @NotNull
    private String date;
    @NotNull
    private String timeOfStart;
    @NotNull
    private String timeOfFinish;

}
