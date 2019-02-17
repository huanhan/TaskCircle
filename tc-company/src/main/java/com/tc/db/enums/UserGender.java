package com.tc.db.enums;


import com.tc.dto.trans.TransEnum;

import java.util.ArrayList;
import java.util.List;

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

    public static List<TransEnum> toList(){
        List<TransEnum> result = new ArrayList<>();
        for (UserGender userGender : UserGender.values()) {
            result.add(TransEnum.init(userGender.name(),userGender.getGender()));
        }
        return result;
    }
}
