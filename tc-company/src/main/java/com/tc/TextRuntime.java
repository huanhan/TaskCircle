package com.tc;

import com.tc.until.IdGenerator;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class TextRuntime {
    public static void main(String[] object){

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        System.out.println(timestamp.getTime());
    }
}
