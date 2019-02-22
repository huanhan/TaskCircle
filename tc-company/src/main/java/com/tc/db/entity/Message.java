package com.tc.db.entity;

import com.google.gson.Gson;
import com.tc.db.enums.MessageState;
import com.tc.db.enums.MessageType;
import com.tc.dto.LongIds;
import com.tc.dto.trans.Trans;
import com.tc.dto.user.QueryUser;
import com.tc.until.FloatHelper;
import com.tc.until.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * @author Cyg
 * 用户
 */
@Entity
public class Message implements Serializable {

    public static final String ID = "id";
    public static final String CONTEXT = "context";
    public static final String TITLE = "title";
    public static final String CREATE_TIME = "createTime";
    public static final String CREATION = "creation";
    public static final String TYPE = "type";
    public static final String STATE = "state";


    private Long id;
    private Long creationId;
    private String context;
    private String title;
    private Timestamp createTime;
    private Admin creation;
    private MessageState state;
    private MessageType type;
    private String lookCondition;

    private long sendCount = 0;
    private long lookCount = 0;

    private Trans transState;
    private Trans transType;


    public static List<Message> toListByIndex(List<Message> content) {
        if (!ListUtils.isEmpty(content)){
            content.forEach(message -> {
                if (message.creation != null){
                    message.setCreation(new Admin(message.getCreation().getUserId(),message.getCreation().getUser()));
                }
                message.setTransState(new Trans(message.getState().name(),message.getState().getState()));
                message.setTransType(new Trans(message.getType().name(),message.getType().getType()));
            });
        }
        return content;
    }

    public static Message toDetail(Message message) {
        if (message != null){
            if (message.getCreation() != null){
                message.setCreation(new Admin(message.getCreation().getUserId(),message.getCreation().getUser()));
            }
            message.setTransState(new Trans(message.getState().name(),message.getState().getState()));
            message.setTransType(new Trans(message.getType().name(),message.getType().getType()));
        }
        return message;
    }



    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "creation")
    public Long getCreationId() {
        return creationId;
    }

    public void setCreationId(Long creationId) {
        this.creationId = creationId;
    }

    @Basic
    @Column(name = "context")
    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    @Basic
    @Column(name = "title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Basic
    @CreationTimestamp
    @Column(name = "create_time")
    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @ManyToOne
    @JoinColumn(name = "creation", referencedColumnName = "user_id", nullable = false,insertable = false,updatable = false)
    public Admin getCreation() {
        return creation;
    }

    public void setCreation(Admin creation) {
        this.creation = creation;
    }

    @Basic
    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    public MessageState getState() {
        return state;
    }

    public void setState(MessageState state) {
        this.state = state;
    }

    @Basic
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    @Basic
    @Column(name = "look_condition")
    public String getLookCondition() {
        return lookCondition;
    }

    public void setLookCondition(String lookCondition) {
        this.lookCondition = lookCondition;
    }

    @Transient
    public long getSendCount() {
        return sendCount;
    }

    public void setSendCount(long sendCount) {
        this.sendCount = sendCount;
    }

    @Transient
    public long getLookCount() {
        return lookCount;
    }

    public void setLookCount(long lookCount) {
        this.lookCount = lookCount;
    }

    @Transient
    public Trans getTransState() {
        return transState;
    }

    public void setTransState(Trans transState) {
        this.transState = transState;
    }

    @Transient
    public Trans getTransType() {
        return transType;
    }

    public void setTransType(Trans transType) {
        this.transType = transType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {return true;}
        if (o == null || getClass() != o.getClass()) {return false;}
        Message message = (Message) o;
        return id.equals(message.getId()) &&
                creation.getUser().getId().equals(message.getCreation().getUser().getId()) &&
                Objects.equals(context, message.context) &&
                Objects.equals(title, message.title) &&
                Objects.equals(createTime, message.createTime) &&
                Objects.equals(state, message.state);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, context, title, createTime, creation, state);
    }

    /**
     * 帅选对应用户可查看的消息
     * @param queryMessage
     * @param user
     * @return
     */
    public static List<Message> byUser(List<Message> queryMessage, User user) {
        List<Message> result = new ArrayList<>();
        if (!ListUtils.isEmpty(queryMessage)){
            queryMessage.forEach(message -> {
                switch (message.getType()){
                    case CONDITION:
                        QueryUser queryUser = new Gson().fromJson(message.lookCondition,QueryUser.class);
                        if (existQuery(queryUser,user)){
                            result.add(message);
                        }
                        break;
                    case ALL:
                        result.add(message);
                        break;
                    case APPOINT:
                        LongIds ids = new Gson().fromJson(message.lookCondition,LongIds.class);
                        if (existIds(ids,user.getId())) {
                            result.add(message);
                        }
                        break;
                    default:
                        break;
                }
            });
        }
        return result;
    }

    /**
     * 判断用户的条件是否满足消息查看的条件
     * @param queryUser
     * @param user
     * @return
     */
    private static boolean existQuery(QueryUser queryUser, User user) {
        boolean exist = false;
        if (queryUser.getState() != null){
            if (user.getState() == null || !queryUser.getState().equals(user.getState())) {
                return false;
            }
        }
        if (queryUser.getCategory() != null){
            if (user.getCategory() == null || !queryUser.getCategory().equals(user.getCategory())){
                return false;
            }
        }
        if (!equalStrByLike(queryUser.getAddress(),user.getAddress())){
            return false;
        }
        if (!equalStrByLike(queryUser.getInterest(),user.getInterest())){
            return false;
        }
        if (!equalStrByLike(queryUser.getIntro(),user.getIntro())){
            return false;
        }
        if (!equalStr(queryUser.getMajor(),user.getMajor())){
            return false;
        }
        if (!equalStr(queryUser.getName(),user.getName())){
            return false;
        }
        if (!equalStr(queryUser.getSchool(),user.getSchool())){
            return false;
        }
        if (!scopeByTime(queryUser.getCreateTimeBegin(),queryUser.getCreateTimeEnd(),user.getCreateTime())){
            return false;
        }
        if (!scopeByTime(queryUser.getBirthdayBegin(),queryUser.getBirthdayEnd(),user.getBirthday())){
            return false;
        }
        if (!scopeByTime(queryUser.getLastLoginBegin(),queryUser.getLastLoginEnd(),user.getLastLogin())){
            return false;
        }
        if (!scopeByFloat(queryUser.getMoneyBegin(),queryUser.getMoneyEnd(),user.getMoney())){
            return false;
        }
        if (!scopeByFloat(queryUser.getHeightBegin().floatValue(),queryUser.getHeightEnd().floatValue(),user.getHeight().floatValue())){
            return false;
        }
        if (!scopeByFloat(queryUser.getWeightBegin().floatValue(),queryUser.getWeightEnd().floatValue(),user.getWeight().floatValue())){
            return false;
        }
        return true;
    }

    /**
     * 判断用户的编号是否存在在同
     * @param ids
     * @param id
     * @return
     */
    private static boolean existIds(LongIds ids, Long id) {
        if (!ListUtils.isEmpty(ids.getIds())){
            for (Long l : ids.getIds()) {
                if (id.equals(l)){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * s2的范围比s1的大
     * s1为空时不判断
     * @param s1
     * @param s2
     * @return
     */
    private static boolean equalStrByLike(String s1,String s2){
        if (StringUtils.isNotEmpty(s1)){
            return !StringUtils.isEmpty(s2) && StringUtils.contains(s2, s1);
        }
        return true;
    }

    /**
     * 判断两个是否相等
     * s1为空时不判断
     * @param s1
     * @param s2
     * @return
     */
    private static boolean equalStr(String s1,String s2){
        if (StringUtils.isNotEmpty(s1)){
            return !StringUtils.isEmpty(s2) && s1.equals(s2);
        }
        return true;
    }

    private static boolean scopeByTime(Timestamp begin,Timestamp end,Timestamp current){
        if (begin != null && end != null){
            if (current == null){
                return false;
            }else {
                if (current.equals(begin) || current.equals(end)){
                    return true;
                }else {
                    return current.after(begin) && current.before(end);
                }
            }
        }else if (begin != null){
            if (current == null){
                return false;
            }else {
                if (current.equals(begin)){
                    return true;
                }else {
                    return current.after(begin);
                }
            }
        }else if (end != null){
            if (current == null){
                return false;
            }else {
                if (current.equals(end)){
                    return true;
                }else {
                    return current.before(end);
                }
            }
        }else {
            return true;
        }
    }

    private static boolean scopeByFloat(Float begin,Float end,Float current){
        if (FloatHelper.isNotNull(begin) && FloatHelper.isNotNull(end)){
            if (FloatHelper.isNotNull(current)){
                return current >= begin && current <= end;
            }else {
                return false;
            }
        }else if (FloatHelper.isNotNull(begin)){
            if (FloatHelper.isNotNull(current)){
                return current >= begin;
            }else {
                return false;
            }
        }else if (FloatHelper.isNotNull(end)){
            if (FloatHelper.isNotNull(current)){
                return current <= end;
            }else {
                return false;
            }
        }else {
            return true;
        }
    }
}
