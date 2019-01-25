package com.tc.until;

import com.tc.db.entity.Comment;
import com.tc.db.entity.HunterTask;
import com.tc.db.enums.CommentType;
import com.tc.db.enums.HunterTaskState;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.criteria.*;
import java.sql.Timestamp;
import java.util.List;

public class QueryUtils {


    public static <X> Subquery<X> selectQuery(Root<?> root,CriteriaQuery<?> query, CriteriaBuilder cb, Class<X> entityClass,String pro,String eq1,String eq2){
        Subquery<X> entity = query.subquery(entityClass);
        Root<X> xRoot = entity.from(entityClass);
        entity.select(xRoot.get(pro));
        Predicate predicate = cb.equal(xRoot.get(eq1),eq2);
        entity.where(predicate);
        return entity;
    }

    /**
     *
     * @param root
     * @param query
     * @param cb
     * @param entityClass
     * @param pro1
     * @param pro2
     * @param <X>
     * @return
     */
    public static <X> Subquery<Integer> countQuery(Root<?> root, CriteriaQuery<?> query, CriteriaBuilder cb, Class<X> entityClass, String pro1, String pro2){
        Subquery<Integer> count = query.subquery(Integer.class);
        Root<X> xRoot = count.from(entityClass);
        count.select(cb.count(xRoot).as(Integer.TYPE));
        Predicate predicate = cb.equal(xRoot.get(pro1),root.get(pro2));
        count.where(predicate);
        return count;
    }

    public static <X> Subquery<Float> avgQuery(Root<?> root, CriteriaQuery<?> query, CriteriaBuilder cb, Class<X> entityClass,String parent,String avgPro, String pro1, String pro2){
        Subquery<Float> count = query.subquery(Float.class);
        Root<X> xRoot = count.from(entityClass);
        count.select(cb.avg(xRoot.get(parent).get(avgPro)).as(Float.TYPE));
        Predicate predicate = cb.equal(xRoot.get(pro1),root.get(pro2));
        count.where(predicate);
        return count;
    }

    public static Predicate between(Root<?> root, CriteriaBuilder cb,String property,Integer begin,Integer end){
        return between(root,cb,property,Float.valueOf(QueryUtils.hasNull(begin)? -1 : begin),Float.valueOf(QueryUtils.hasNull(end)? -1 : end));
    }

    public static Predicate between(Root<?> root, CriteriaBuilder cb, String property, Timestamp begin, Timestamp end){
        Predicate predicate = null;
        if (begin != null && end != null){
            predicate = cb.between(root.get(property),begin ,end);
        }else if (begin != null){
            predicate = cb.greaterThan(root.get(property),begin);
        }else if (end != null){
            predicate = cb.lessThan(root.get(property),end);
        }
        return predicate;
    }

    public static <T> Predicate between(Path<?> root, CriteriaBuilder cb, Timestamp begin, Timestamp end) {
        Predicate predicate = null;
        if (begin != null && end != null){
            predicate = cb.between((Expression<? extends Timestamp>) root, begin, end);
        }else if (begin != null){
            predicate = cb.greaterThan((Expression<? extends Timestamp>) root, begin);
        }else if (end != null){
            predicate = cb.lessThan((Expression<? extends Timestamp>) root, end);
        }
        return predicate;
    }

    public static Predicate between(Root<?> root, CriteriaBuilder cb,String property,Float begin,Float end){
        Predicate predicate = null;
        if (!QueryUtils.hasNull(begin) && !QueryUtils.hasNull(end)){
            predicate = cb.between(root.get(property),begin ,end);
        }else if (!QueryUtils.hasNull(begin)){
            predicate = cb.greaterThan(root.get(property),begin);
        }else if (!QueryUtils.hasNull(end)){
            predicate = cb.lessThan(root.get(property),end);
        }
        return predicate;
    }

    public static <Y> Predicate between(Path<?> root, CriteriaBuilder cb, Float begin, Float end) {
        Predicate predicate = null;
        if (!QueryUtils.hasNull(begin) && !QueryUtils.hasNull(end)){
            predicate = cb.between((Expression<? extends Float>) root,begin ,end);
        }else if (!QueryUtils.hasNull(begin)){
            predicate = cb.greaterThan((Expression<? extends Float>) root,begin);
        }else if (!QueryUtils.hasNull(end)){
            predicate = cb.lessThan((Expression<? extends Float>) root,end);
        }
        return predicate;
    }

    public static Predicate between(CriteriaBuilder cb,Subquery<Integer> count,Integer begin,Integer end){
        Predicate predicate = null;
        if (!QueryUtils.hasNull(begin) && !QueryUtils.hasNull(end)){
            predicate = cb.between(count,begin ,end);
        }else if (!QueryUtils.hasNull(begin)){
            predicate = cb.greaterThan(count,begin);
        }else if (!QueryUtils.hasNull(end)){
            predicate = cb.lessThan(count,end);
        }
        return predicate;
    }

    public static Predicate between(CriteriaBuilder cb, Subquery<Float> avg, Float begin, Float end) {
        Predicate predicate = null;
        if (!QueryUtils.hasNull(begin) && !QueryUtils.hasNull(end)){
            predicate = cb.between(avg,begin ,end);
        }else if (!QueryUtils.hasNull(begin)){
            predicate = cb.greaterThan(avg,begin);
        }else if (!QueryUtils.hasNull(end)){
            predicate = cb.lessThan(avg,end);
        }
        return predicate;
    }



    public static Predicate like(Root<?> root, CriteriaBuilder cb,String property,String value){
        Predicate predicate = null;
        if (!StringUtils.isEmpty(value)){
            predicate = cb.like(root.get(property),"%" + value + "%");
        }
        return predicate;
    }

    public static <T> Predicate like(Path<?> root, CriteriaBuilder cb, T value) {
        Predicate predicate = null;
        if (value != null){
            predicate = cb.like((Expression<String>) root,"%" + value + "%");
        }
        return predicate;
    }

    public static Predicate equals(Root<?> root, CriteriaBuilder cb,String property,String value){
        Predicate predicate = null;
        if (!StringUtils.isEmpty(value)){
            predicate = cb.equal(root.get(property),value);
        }
        return predicate;
    }

    public static <T> Predicate equals(Root<?> root, CriteriaBuilder cb,String property,T value){
        Predicate predicate = null;
        if (value != null){
            predicate = cb.equal(root.get(property),value);
        }
        return predicate;
    }

    public static <T> Predicate equals(Path<?> root, CriteriaBuilder cb,T value){
        Predicate predicate = null;
        if (value != null){
            predicate = cb.equal(root,value);
        }
        return predicate;
    }

//    public static Predicate in(Path<?> root, CriteriaBuilder cb, String property, List<Long> value){
//        Predicate predicate = null;
//        if (value != null){
//            predicate = cb.in(root.get(property));
//            for (Long i:
//                    value) {
//                predicate.in(i);
//            }
//        }
//        return predicate;
//    }

    public static <T> Predicate in(Path<?> root, CriteriaBuilder cb, String property, List<T> value){
        Predicate predicate = null;
        if (value != null){
            predicate = cb.in(root.get(property)).value(value);
//            for (T i:
//                    value) {
//                predicate.in(i);
//            }
        }
        return predicate;
    }

    public static <T> Predicate in(Path<?> root, CriteriaBuilder cb, String property, Expression<T> value){
        Predicate predicate = null;
        if (value != null){
            predicate = cb.in(root.get(property)).value(value);
        }
        return predicate;
    }

    /**
     * 用户判断数值型
     * @param value
     * @return
     */
    public static boolean hasNull(Object value){
        if (value != null){
            if (value instanceof Integer || value instanceof Float){
                if (value instanceof Integer){
                    Integer v = (Integer) value;
                    return v < 0;
                }
                Float v = (Float) value;
                return v < 0;

            }
        }
        return true;
    }



}
