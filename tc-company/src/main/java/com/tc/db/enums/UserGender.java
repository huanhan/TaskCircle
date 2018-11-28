package com.tc.db.enums;


/**
 * @author Cyg
 * 用户性别
 */

public enum  UserGender {

    MAN("男"),
    WOMAN("女")
    ;

    private String gender;

    UserGender(String gender) {
        this.gender = gender;
    }

    public String getGender() {
        return gender;
    }
}
