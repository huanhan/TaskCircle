package com.tc.db.enums;

public enum  AdminState {

    ON_GUARD("在岗"),
    LEAVE_FOOICE("离职")
    ;


    private String state;

    AdminState(String state) {
        this.state = state;
    }

}
