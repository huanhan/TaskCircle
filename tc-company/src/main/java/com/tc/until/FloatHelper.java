package com.tc.until;

import java.math.BigDecimal;

public class FloatHelper {

    /**
     * 减法
     * @param f1
     * @param f2
     * @return
     */
    public static Float sub(Float f1,Float f2){
        BigDecimal b1 = new BigDecimal(f1.toString());
        BigDecimal b2 = new BigDecimal(f2.toString());
        return b1.subtract(b2).floatValue();
    }

    /**
     * 加法
     * @param f1
     * @param f2
     * @return
     */
    public static Float add(Float f1,Float f2){
        BigDecimal b1 = new BigDecimal(f1.toString());
        BigDecimal b2 = new BigDecimal(f2.toString());
        return b1.add(b2).floatValue();
    }

    /**
     * 除法
     * @param f1
     * @param f2
     * @return
     */
    public static Float divied(Float f1,Float f2){
        BigDecimal b1 = new BigDecimal(f1.toString());
        BigDecimal b2 = new BigDecimal(f2.toString());
        return b1.divide(b2,3,0).floatValue();
    }

    /**
     * 乘法
     * @param f1
     * @param f2
     * @return
     */
    public static Float multiply(Float f1,Float f2){
        BigDecimal b1 = new BigDecimal(f1.toString());
        BigDecimal b2 = new BigDecimal(f2.toString());
        return b1.multiply(b2).floatValue();
    }

    /**
     * 减法
     * @param f1
     * @param f2
     * @return
     */
    public static BigDecimal subToBD(Float f1,Float f2){
        BigDecimal b1 = new BigDecimal(f1.toString());
        BigDecimal b2 = new BigDecimal(f2.toString());
        return b1.subtract(b2);
    }

    /**
     * 加法
     * @param f1
     * @param f2
     * @return
     */
    public static BigDecimal addToBD(Float f1,Float f2){
        BigDecimal b1 = new BigDecimal(f1.toString());
        BigDecimal b2 = new BigDecimal(f2.toString());
        return b1.add(b2);
    }

    /**
     * 除法
     * @param f1
     * @param f2
     * @return
     */
    public static BigDecimal diviedToBD(Float f1,Float f2){
        BigDecimal b1 = new BigDecimal(f1.toString());
        BigDecimal b2 = new BigDecimal(f2.toString());
        return b1.divide(b2,3,0);
    }

    /**
     * 乘法
     * @param f1
     * @param f2
     * @return
     */
    public static BigDecimal multiplyToBD(Float f1,Float f2){
        BigDecimal b1 = new BigDecimal(f1.toString());
        BigDecimal b2 = new BigDecimal(f2.toString());
        return b1.multiply(b2);
    }

    public static BigDecimal toBig(Float f){
        return new BigDecimal(f.toString());
    }

    /**
     * 判断值是不是空
     * @param value
     * @return
     */
    public static boolean isNull(Long value) {
        return value == null || value <= 0;
    }

    /**
     * 判断值是不是空
     * @param value
     * @return
     */
    public static boolean isNull(Float value){
        return value == null || value <= 0;
    }

    /**
     * 判断值是不是非空
     * @param value
     * @return
     */
    public static boolean isNotNull(Float value){
        return !isNull(value);
    }
}
