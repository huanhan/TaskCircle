package com.tc;

import com.tc.validator.until.IdGenerator;

public class TextRuntime {
    public static void main(String[] object){
        System.out.println(IdGenerator.INSTANCE.nextId());
    }
}
