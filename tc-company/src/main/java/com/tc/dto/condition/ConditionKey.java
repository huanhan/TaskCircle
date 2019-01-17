package com.tc.dto.condition;

import com.tc.db.entity.User;
import com.tc.db.enums.UserCategory;
import com.tc.db.enums.UserState;
import org.hibernate.usertype.UserType;

import java.util.ArrayList;
import java.util.List;

public class ConditionKey {
    private String key;
    private String value;
    private List<ConditionKey> keys;

    public ConditionKey() {
    }

    public ConditionKey(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public ConditionKey(String key, String value, List<ConditionKey> keys) {
        this.key = key;
        this.value = value;
        this.keys = keys;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public List<ConditionKey> getKeys() {
        return keys;
    }

    public void setKeys(List<ConditionKey> keys) {
        this.keys = keys;
    }



    public static List<ConditionKey> init(){
        List<ConditionKey> result = new ArrayList<>();
        result.add(new ConditionKey(User.ID,"用户编号"));
        result.add(new ConditionKey(User.STATE,"用户状态",UserState.toList()));
        result.add(new ConditionKey(User.CATEGORY,"用户分类",UserCategory.toList()));
        return result;
    }

}
