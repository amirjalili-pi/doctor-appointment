package com.blubank.doctorappointment.validation;

import com.blubank.doctorappointment.domain.model.dto.base.ARequestBaseDto;

public interface IRequestValidator<R extends ARequestBaseDto>{

    boolean validateRequest(R request);


}
