package com.tc;

import com.tc.until.IdGenerator;
import com.tc.until.TelephoneUtil;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class TextRuntime {
    public static void main(String[] object){
        System.out.println(IdGenerator.INSTANCE.nextId());

        Float f1 = 2f;
        Float f2 = 1.2f;

        BigDecimal b1 = new BigDecimal(f1.toString());
        BigDecimal b2 = new BigDecimal(f2.toString());

        System.out.println(b1.subtract(b2).floatValue());
        System.out.println(b1.add(b2).floatValue());
        System.out.println(b1.multiply(b2).floatValue());
        System.out.println(b1.remainder(b2).floatValue());
        System.out.println(b1.divide(b2,3,0).floatValue());

    }
}
