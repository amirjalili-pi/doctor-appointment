package com.blubank.doctorappointment.domain.enumeration;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum ActionTypeEnum {
    Insert(0),
    Update(1),
    Delete(2);

    ActionTypeEnum(int ModifyActionTypeCode) {
        this.ModifyActionTypeCode = ModifyActionTypeCode;
    }

    int ModifyActionTypeCode;

    @JsonValue
    public int getModifyActionTypeCode() {
        return ModifyActionTypeCode;
    }

    public void setModifyActionTypeCode(int ModifyActionTypeCode) {
        if(ModifyActionTypeCode < 0 || ModifyActionTypeCode > 1){
            throw new IllegalArgumentException("The given Action type code is invalid.");
        }

        this.ModifyActionTypeCode = ModifyActionTypeCode;
    }

    public static List<ActionTypeEnum> getAllModifyActionTypes(){
        List<ActionTypeEnum> retVal;
        retVal = new ArrayList<ActionTypeEnum>(Arrays.asList(ActionTypeEnum.values()));
        return retVal;
    }

    @Override
    public String toString() {
        switch (this) {
            case Insert:
                return "Insert";
            case Update:
                return "Update";
            case Delete:
                return "Delete";
            default:
                return "Undefined";
        }
    }
}
