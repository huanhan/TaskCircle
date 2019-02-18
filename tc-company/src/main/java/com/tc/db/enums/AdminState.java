package com.tc.db.enums;

import com.tc.dto.trans.TransEnum;

import java.util.ArrayList;
import java.util.List;

public enum  AdminState {

    ON_GUARD("在岗"),
    LEAVE_FOOICE("离职")
    ;


    private String state;

    AdminState(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }


    public static List<TransEnum> toList(){
        List<TransEnum> result = new ArrayList<>();
        for (AdminState adminState : AdminState.values()) {
            result.add(TransEnum.init(adminState.name(),adminState.getState()));
        }
        return result;
    }
}
