package com.tc.db.enums;

import com.tc.dto.Show;
import com.tc.dto.TransEnum;
import com.tc.dto.trans.Trans;
import com.tc.until.ListUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Cyg
 * 用户类别
 */

public enum UserCategory {

    NORMAL("普通用户"),
    HUNTER("猎刃"),
    SUPPER_ADMIN("超级管理员"),
    ADMINISTRATOR("管理员"),
    DEVELOPER("开发人员")
    ;

    private String category;

    UserCategory(String category) {
        this.category = category;
    }



    public String getCategory() {
        return category;
    }


    public static List<UserCategory> allByUser(){

        List<UserCategory> userCategories = new ArrayList<>();
        userCategories.add(UserCategory.NORMAL);
        userCategories.add(UserCategory.HUNTER);

        return userCategories;
    }

    public static List<Show> toShows(List<UserCategory> list){
        List<Show> result = new ArrayList<>();
        if (!list.isEmpty()){
            list.forEach(userCategory -> {
                result.add(new Show(userCategory.name(),userCategory.getCategory()));
            });
        }
        return result;
    }

    public static UserCategory findByName(String name){
        UserCategory result = null;
        try {
            result = UserCategory.valueOf(name);
        }catch (IllegalArgumentException ignored){

        }
        return result;
    }

    public static List<TransEnum> toList() {
        List<TransEnum> result = new ArrayList<>();
        for (UserCategory userCategory : UserCategory.values()) {
            if (!userCategory.equals(UserCategory.SUPPER_ADMIN)){
                result.add(TransEnum.init(userCategory.name(),userCategory.getCategory()));
            }
        }
        return result;
    }

    public static List<Trans> toTrans(List<UserCategory> list) {
        List<Trans> result = new ArrayList<>();
        if (ListUtils.isNotEmpty(list)){
            list.forEach(userCategory -> {
                result.add(new Trans(userCategory.name(),userCategory.getCategory()));
            });
        }
        return result;
    }


}
