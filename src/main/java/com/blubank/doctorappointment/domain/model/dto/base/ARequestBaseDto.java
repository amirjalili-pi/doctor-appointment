package com.blubank.doctorappointment.domain.model.dto.base;

import com.blubank.doctorappointment.domain.model.dto.ErrorObjectDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public abstract class ARequestBaseDto implements Serializable {
    @JsonIgnore
    private List<ErrorObjectDto> errorObjects = new ArrayList<>();



    public abstract String getKey();

}
