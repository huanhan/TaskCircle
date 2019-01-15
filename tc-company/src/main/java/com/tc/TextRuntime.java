package com.tc;

import com.tc.until.IdGenerator;
import com.tc.until.TelephoneUtil;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class TextRuntime {
    public static void main(String[] object){

        System.out.print(IdGenerator.INSTANCE.nextId());
    }
}
