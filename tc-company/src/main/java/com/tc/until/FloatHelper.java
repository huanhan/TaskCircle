package com.tc.until;

import java.math.BigDecimal;

public class FloatHelper {

    public static Float sub(Float f1,Float f2){
        BigDecimal b1 = new BigDecimal(f1.toString());
        BigDecimal b2 = new BigDecimal(f2.toString());
        return b1.subtract(b2).floatValue();
    }

    public static Float add(Float f1,Float f2){
        BigDecimal b1 = new BigDecimal(f1.toString());
        BigDecimal b2 = new BigDecimal(f2.toString());
        return b1.add(b2).floatValue();
    }

    public static Float divied(Float f1,Float f2){
        BigDecimal b1 = new BigDecimal(f1.toString());
        BigDecimal b2 = new BigDecimal(f2.toString());
        return b1.divide(b2,3,0).floatValue();
    }

    public static Float multiply(Float f1,Float f2){
        BigDecimal b1 = new BigDecimal(f1.toString());
        BigDecimal b2 = new BigDecimal(f2.toString());
        return b1.multiply(b2).floatValue();
    }
}
