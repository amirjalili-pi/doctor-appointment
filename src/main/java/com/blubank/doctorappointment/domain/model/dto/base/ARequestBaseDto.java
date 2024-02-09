package com.blubank.doctorappointment.domain.model.dto.base;

import com.blubank.doctorappointment.domain.model.dto.ErrorObjectDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static io.swagger.v3.oas.annotations.media.Schema.AccessMode.READ_ONLY;

@Getter
@Setter
public abstract class ARequestBaseDto implements Serializable {
    @JsonIgnore
    @Schema(accessMode = READ_ONLY)
    private List<ErrorObjectDto> errorObjects = new ArrayList<>();



    @JsonIgnore
    @Schema(accessMode = READ_ONLY)
    public abstract String getKey();

}
