package com.tc.dto.user.hunter;

import com.tc.db.entity.*;
import com.tc.dto.user.QueryUser;
import com.tc.until.QueryUtils;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.criteria.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * 查询猎刃
 * @author Cyg
 */
public class QueryHunter extends QueryUser {

    private Timestamp hunterCreateBegin;
    private Timestamp hunterCreateEnd;

    private Integer hunterTaskCountBegin;

    private Integer hunterTaskCountEnd;

    private Float hunterCommentAvgBegin;

    private Float hunterCommentAvgEnd;

    public Timestamp getHunterCreateBegin() {
        return hunterCreateBegin;
    }

    public void setHunterCreateBegin(Timestamp hunterCreateBegin) {
        this.hunterCreateBegin = hunterCreateBegin;
    }

    public Timestamp getHunterCreateEnd() {
        return hunterCreateEnd;
    }

    public void setHunterCreateEnd(Timestamp hunterCreateEnd) {
        this.hunterCreateEnd = hunterCreateEnd;
    }

    public Integer getHunterTaskCountBegin() {
        return hunterTaskCountBegin;
    }

    public void setHunterTaskCountBegin(Integer hunterTaskCountBegin) {
        this.hunterTaskCountBegin = hunterTaskCountBegin;
    }

    public Integer getHunterTaskCountEnd() {
        return hunterTaskCountEnd;
    }

    public void setHunterTaskCountEnd(Integer hunterTaskCountEnd) {
        this.hunterTaskCountEnd = hunterTaskCountEnd;
    }

    public Float getHunterCommentAvgBegin() {
        return hunterCommentAvgBegin;
    }

    public void setHunterCommentAvgBegin(Float hunterCommentAvgBegin) {
        this.hunterCommentAvgBegin = hunterCommentAvgBegin;
    }

    public Float getHunterCommentAvgEnd() {
        return hunterCommentAvgEnd;
    }

    public void setHunterCommentAvgEnd(Float hunterCommentAvgEnd) {
        this.hunterCommentAvgEnd = hunterCommentAvgEnd;
    }

    public static List<Predicate> initPredicates(QueryHunter queryHunter, Root<Hunter> root, CriteriaQuery<?> query, CriteriaBuilder cb){
        List<Predicate> predicates = new ArrayList<>();
        if (!StringUtils.isEmpty(queryHunter.getName())){
            predicates.add(cb.equal(root.get(Hunter.USER).get(User.NAME),queryHunter.getName()));
        }
        if (!StringUtils.isEmpty(queryHunter.getUsername())){
            predicates.add(cb.equal(root.get(Hunter.USER).get(User.USERNAME),queryHunter.getUsername()));
        }
        if (queryHunter.getGender() != null){
            predicates.add(cb.equal(root.get(Hunter.USER).get(User.GENDER),queryHunter.getGender()));
        }
        if (queryHunter.getCategory() != null){
            predicates.add(cb.equal(root.get(Hunter.USER).get(User.CATEGORY),queryHunter.getCategory()));
        }
        if (queryHunter.getState() != null){
            predicates.add(cb.equal(root.get(Hunter.USER).get(User.STATE),queryHunter.getState()));
        }
        if (!StringUtils.isEmpty(queryHunter.getAddress())){
            predicates.add(cb.like(root.get(Hunter.USER).get(User.ADDRESS),"%" + queryHunter.getAddress() + "%"));
        }
        if (!StringUtils.isEmpty(queryHunter.getIdCard())){
            predicates.add(cb.like(root.get(Hunter.USER).get(User.ID_CARD),"%" + queryHunter.getIdCard() + "%"));
        }
        if (!StringUtils.isEmpty(queryHunter.getSchool())){
            predicates.add(cb.like(root.get(Hunter.USER).get(User.SCHOOL),"%" + queryHunter.getSchool() + "%"));
        }
        if (!StringUtils.isEmpty(queryHunter.getMajor())){
            predicates.add(cb.like(root.get(Hunter.USER).get(User.MAJOR),"%" + queryHunter.getMajor() + "%"));
        }
        if (!StringUtils.isEmpty(queryHunter.getInterest())){
            predicates.add(cb.like(root.get(Hunter.USER).get(User.INTEREST),"%" + queryHunter.getInterest() + "%"));
        }
        if (!StringUtils.isEmpty(queryHunter.getInterest())){
            predicates.add(cb.like(root.get(Hunter.USER).get(User.INTRO),"%" + queryHunter.getInterest() + "%"));
        }
        if (queryHunter.getLastLoginBegin() != null || queryHunter.getLastLoginEnd() != null){
            if (queryHunter.getLastLoginBegin() != null && queryHunter.getLastLoginEnd() != null){
                predicates.add(cb.between(root.get(Hunter.USER).get(User.LAST_LOGIN),queryHunter.getLastLoginBegin(),queryHunter.getLastLoginEnd()));
            }else if (queryHunter.getLastLoginBegin() != null){
                predicates.add(cb.greaterThan(root.get(Hunter.USER).get(User.LAST_LOGIN),queryHunter.getLastLoginBegin()));
            }else if (queryHunter.getLastLoginEnd() != null){
                predicates.add(cb.lessThan(root.get(Hunter.USER).get(User.LAST_LOGIN),queryHunter.getLastLoginEnd()));
            }
        }
        if (queryHunter.getCreateTimeBegin() != null || queryHunter.getCreateTimeEnd() != null){
            if (queryHunter.getCreateTimeBegin()  != null && queryHunter.getCreateTimeEnd() != null){
                predicates.add(cb.between(root.get(Hunter.USER).get(User.CREATE_TIME),queryHunter.getCreateTimeBegin() ,queryHunter.getCreateTimeEnd()));
            }else if (queryHunter.getCreateTimeBegin()  != null){
                predicates.add(cb.greaterThan(root.get(Hunter.USER).get(User.CREATE_TIME),queryHunter.getCreateTimeBegin() ));
            }else if (queryHunter.getCreateTimeEnd() != null){
                predicates.add(cb.lessThan(root.get(Hunter.USER).get(User.CREATE_TIME),queryHunter.getCreateTimeEnd()));
            }
        }
        if (queryHunter.getHeightBegin() != null || queryHunter.getHeightEnd() != null){
            if (queryHunter.getHeightBegin()  != null && queryHunter.getHeightEnd() != null && queryHunter.getHeightBegin() >= 0 && queryHunter.getHeightEnd() >= 0){
                predicates.add(cb.between(root.get(Hunter.USER).get(User.HEIGHT),queryHunter.getHeightBegin() ,queryHunter.getHeightEnd()));
            }else if (queryHunter.getHeightBegin()  != null && queryHunter.getHeightBegin() >= 0){
                predicates.add(cb.greaterThan(root.get(Hunter.USER).get(User.HEIGHT),queryHunter.getHeightBegin() ));
            }else if (queryHunter.getHeightEnd() != null && queryHunter.getHeightEnd() >= 0){
                predicates.add(cb.lessThan(root.get(Hunter.USER).get(User.HEIGHT),queryHunter.getHeightEnd()));
            }
        }
        if (queryHunter.getWeightBegin() != null || queryHunter.getWeightEnd() != null){
            if (queryHunter.getWeightBegin()  != null && queryHunter.getWeightEnd() != null && queryHunter.getWeightBegin() >= 0 && queryHunter.getWeightEnd() >= 0){
                predicates.add(cb.between(root.get(Hunter.USER).get(User.WEIGHT),queryHunter.getWeightBegin() ,queryHunter.getWeightEnd()));
            }else if (queryHunter.getWeightBegin()  != null && queryHunter.getWeightBegin() >= 0){
                predicates.add(cb.greaterThan(root.get(Hunter.USER).get(User.WEIGHT),queryHunter.getWeightBegin() ));
            }else if (queryHunter.getWeightEnd() != null && queryHunter.getWeightEnd() >= 0){
                predicates.add(cb.lessThan(root.get(Hunter.USER).get(User.WEIGHT),queryHunter.getWeightEnd()));
            }
        }
        if (queryHunter.getBirthdayBegin() != null || queryHunter.getBirthdayEnd() != null){
            if (queryHunter.getBirthdayBegin()  != null && queryHunter.getBirthdayEnd() != null){
                predicates.add(cb.between(root.get(Hunter.USER).get(User.BIRTHDAY),queryHunter.getBirthdayBegin() ,queryHunter.getBirthdayEnd()));
            }else if (queryHunter.getBirthdayBegin()  != null){
                predicates.add(cb.greaterThan(root.get(Hunter.USER).get(User.BIRTHDAY),queryHunter.getBirthdayBegin() ));
            }else if (queryHunter.getBirthdayEnd() != null){
                predicates.add(cb.lessThan(root.get(Hunter.USER).get(User.BIRTHDAY),queryHunter.getBirthdayEnd()));
            }
        }
        if (queryHunter.getMoneyBegin() != null || queryHunter.getMoneyEnd() != null){
            if (queryHunter.getMoneyBegin()  != null && queryHunter.getMoneyEnd() != null && queryHunter.getMoneyBegin() >= 0 && queryHunter.getMoneyEnd() >= 0){
                predicates.add(cb.between(root.get(Hunter.USER).get(User.MONEY),queryHunter.getMoneyBegin() ,queryHunter.getMoneyEnd()));
            }else if (queryHunter.getMoneyBegin()  != null && queryHunter.getMoneyBegin() >= 0){
                predicates.add(cb.greaterThan(root.get(Hunter.USER).get(User.MONEY),queryHunter.getMoneyBegin() ));
            }else if (queryHunter.getMoneyEnd() != null && queryHunter.getMoneyEnd() >= 0){
                predicates.add(cb.lessThan(root.get(Hunter.USER).get(User.MONEY),queryHunter.getMoneyEnd()));
            }
        }
        if (queryHunter.getHunterCreateBegin() != null || queryHunter.getHunterCreateEnd() != null){
            if (queryHunter.getHunterCreateBegin()  != null && queryHunter.getHunterCreateEnd() != null){
                predicates.add(cb.between(root.get(Hunter.CREATE_TIME),queryHunter.getHunterCreateBegin() ,queryHunter.getHunterCreateEnd()));
            }else if (queryHunter.getHunterCreateBegin()  != null){
                predicates.add(cb.greaterThan(root.get(Hunter.CREATE_TIME),queryHunter.getHunterCreateBegin() ));
            }else if (queryHunter.getHunterCreateEnd() != null){
                predicates.add(cb.lessThan(root.get(Hunter.CREATE_TIME),queryHunter.getHunterCreateEnd()));
            }
        }
        if (!QueryUtils.hasNull(queryHunter.getHunterTaskCountBegin()) || !QueryUtils.hasNull(queryHunter.getHunterTaskCountEnd())){
            Subquery<Integer> count = QueryUtils.countQuery(root,query,cb,HunterTask.class,HunterTask.HUNTER_ID,User.ID);
            predicates.add(QueryUtils.between(cb,count,queryHunter.getHunterTaskCountBegin(),queryHunter.getHunterTaskCountEnd()));
        }

        if (!QueryUtils.hasNull(queryHunter.getHunterCommentAvgBegin()) || !QueryUtils.hasNull(queryHunter.getHunterCommentAvgEnd())){
            Subquery<Float> avg = QueryUtils.avgQuery(root,query,cb,CommentHunter.class,CommentHunter.COMMENT,Comment.NUMBER,CommentHunter.HUNTER_ID,User.ID);
            predicates.add(QueryUtils.between(cb,avg,queryHunter.getCommentAvgBegin(),queryHunter.getCommentAvgEnd()));
        }
        return predicates;
    }

}
