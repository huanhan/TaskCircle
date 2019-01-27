package com.tc.dto.finance;

import com.tc.db.entity.Comment;
import com.tc.db.entity.User;
import com.tc.db.entity.UserIeRecord;
import com.tc.dto.enums.IEType;
import com.tc.until.FloatHelper;
import com.tc.until.ListUtils;
import com.tc.until.PageRequest;
import com.tc.until.QueryUtils;
import org.springframework.data.domain.Sort;

import javax.persistence.criteria.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 查询用户转账信息
 * @author Cyg
 */
public class QueryIE extends PageRequest {

    private String id;
    private Timestamp createTimeBegin;
    private Timestamp createTimeEnd;
    private IEType ieType;
    private Float moneyBegin;
    private Float moneyEnd;
    private String context;
    private Long me;
    private String meName;
    private String meAccount;
    private Long to;
    private String toName;
    private String toAccount;

    public QueryIE() {
        super(0, 10);
    }

    public QueryIE(int page, int size) {
        super(page, size);
    }

    public QueryIE(int page, int size, Sort.Direction direction, String... properties) {
        super(page, size, direction, properties);
    }

    public QueryIE(int page, int size, Sort sort) {
        super(page, size, sort);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Timestamp getCreateTimeBegin() {
        return createTimeBegin;
    }

    public void setCreateTimeBegin(Timestamp createTimeBegin) {
        this.createTimeBegin = createTimeBegin;
    }

    public Timestamp getCreateTimeEnd() {
        return createTimeEnd;
    }

    public void setCreateTimeEnd(Timestamp createTimeEnd) {
        this.createTimeEnd = createTimeEnd;
    }

    public Float getMoneyBegin() {
        return moneyBegin;
    }

    public void setMoneyBegin(Float moneyBegin) {
        this.moneyBegin = moneyBegin;
    }

    public Float getMoneyEnd() {
        return moneyEnd;
    }

    public void setMoneyEnd(Float moneyEnd) {
        this.moneyEnd = moneyEnd;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public Long getMe() {
        return me;
    }

    public void setMe(Long me) {
        this.me = me;
    }

    public Long getTo() {
        return to;
    }

    public void setTo(Long to) {
        this.to = to;
    }

    public String getMeName() {
        return meName;
    }

    public void setMeName(String meName) {
        this.meName = meName;
    }

    public String getMeAccount() {
        return meAccount;
    }

    public void setMeAccount(String meAccount) {
        this.meAccount = meAccount;
    }

    public String getToName() {
        return toName;
    }

    public void setToName(String toName) {
        this.toName = toName;
    }

    public String getToAccount() {
        return toAccount;
    }

    public void setToAccount(String toAccount) {
        this.toAccount = toAccount;
    }

    public IEType getIeType() {
        return ieType;
    }

    public void setIeType(IEType ieType) {
        this.ieType = ieType;
    }

    public static List<Predicate> initPredicates(QueryIE queryIE, Root<UserIeRecord> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(QueryUtils.equals(root,cb,UserIeRecord.ID,queryIE.id));

        if (queryIE.getIeType() != null){
            switch (queryIE.getIeType()){
                case OUT:
                    predicates.add(cb.equal(root.get(UserIeRecord.TO), queryIE.to));
                    predicates.add(QueryUtils.equals(root.get(UserIeRecord.USER_TO).get(User.NAME),cb,queryIE.toName));
                    predicates.add(QueryUtils.equals(root.get(UserIeRecord.USER_TO).get(User.USERNAME),cb,queryIE.toAccount));
                    predicates.add(cb.equal(root.get(UserIeRecord.ME), queryIE.me));
                    break;
                case IN:
                    predicates.add(cb.equal(root.get(UserIeRecord.ME), queryIE.me));
                    predicates.add(QueryUtils.equals(root.get(UserIeRecord.USER_ME).get(User.NAME),cb,queryIE.meName));
                    predicates.add(QueryUtils.equals(root.get(UserIeRecord.USER_ME).get(User.USERNAME),cb,queryIE.meAccount));
                    predicates.add(cb.equal(root.get(UserIeRecord.TO), queryIE.to));
                    break;
                default:
                    break;
            }
        }else {


            if (!FloatHelper.isNull(queryIE.getMe()) && !FloatHelper.isNull(queryIE.getTo())) {
                predicates.add(cb.or(cb.equal(root.get(UserIeRecord.ME), queryIE.me), cb.equal(root.get(UserIeRecord.TO), queryIE.to)));
            } else if (!FloatHelper.isNull(queryIE.getMe())) {
                predicates.add(cb.equal(root.get(UserIeRecord.ME), queryIE.me));
            } else {
                predicates.add(cb.equal(root.get(UserIeRecord.TO), queryIE.to));
            }

        }
        predicates.add(QueryUtils.like(root,cb,UserIeRecord.CONTEXT,queryIE.context));
        predicates.add(QueryUtils.between(root,cb,UserIeRecord.MONEY,queryIE.getMoneyBegin(),queryIE.getMoneyEnd()));
        predicates.add(QueryUtils.between(root,cb,UserIeRecord.CREATE_TIME,queryIE.createTimeBegin,queryIE.createTimeEnd));


        predicates.removeIf(Objects::isNull);

        return predicates;
    }

    public static QueryIE model(){
        QueryIE queryIE = new QueryIE();
        queryIE.setTo(1L);
        queryIE.setMe(1L);
        queryIE.setContext("");
        queryIE.setCreateTimeBegin(null);
        queryIE.setCreateTimeEnd(null);
        queryIE.setMoneyBegin(1F);
        queryIE.setMoneyEnd(1F);
        queryIE.setId("");
        return queryIE;
    }
}
