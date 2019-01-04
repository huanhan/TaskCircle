package com.tc.until;

import org.springframework.data.jpa.domain.Specification;

import java.sql.Timestamp;
import java.util.Collection;

public final class SpecificationFactory {

    /**
     * 模糊查询，匹配对应字段
     * @param attribute
     * @param value
     * @return
     */
    public static Specification containsLike(String attribute,String value){
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.like(root.get(attribute),"%" + value + "%");
    }

    /**
     * 模糊查询，匹配对应字段
     * @param attribute
     * @param value
     * @return
     */
    public static Specification equal(String attribute,String value){
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.like(root.get(attribute), value);
    }


    /**
     * 获取对应属性的值所在的区间
     * @param attribute
     * @param min 最小值
     * @param max 最大值
     * @return
     */
    public static Specification isBetween(String attribute,int min,int max){
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.between(root.get(attribute), min,max);
    }

    /**
     * 获取对应属性的值所在的区间
     * @param attribute
     * @param min 最小值
     * @param max 最大值
     * @return
     */
    public static Specification isBetween(String attribute,double min,double max){
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.between(root.get(attribute), min,max);
    }

    /**
     * 获取对应属性的值所在的区间
     * @param attribute
     * @param min 最小日期
     * @param max 最大日期
     * @return
     */
    public static Specification isBetween(String attribute, Timestamp min, Timestamp max){
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.between(root.get(attribute), min,max);
    }

    /**
     * 通过属性名和集合in查询
     * @param attribute
     * @param value
     * @return
     */
    public static Specification in(String attribute,Collection value){
        return (root, criteriaQuery, criteriaBuilder) -> root.get(attribute).in(value);
    }

    /**
     * 获取对应属性的值所在的区间
     * @param attribute
     * @param value
     * @return
     */
    public static Specification greaterThan(String attribute, Float value){
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.greaterThan(root.get(attribute), value);
    }
}
