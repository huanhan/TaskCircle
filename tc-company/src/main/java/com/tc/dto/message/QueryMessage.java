package com.tc.dto.message;

import com.tc.db.entity.Admin;
import com.tc.db.entity.Message;
import com.tc.db.entity.User;
import com.tc.db.entity.Message;
import com.tc.db.enums.MessageState;
import com.tc.db.enums.MessageType;
import com.tc.until.PageRequest;
import com.tc.until.QueryUtils;
import org.springframework.data.domain.Sort;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 查询消息
 * @author Cyg
 */
public class QueryMessage extends PageRequest {

    private Long id;
    private String context;
    private String title;
    private Timestamp createTimeBegin;
    private Timestamp createTimeEnd;
    private Long creation;
    private String creationName;
    private String account;
    private MessageType type;
    private MessageState state;


    public QueryMessage() {
        super(0,10);
    }

    public QueryMessage(int page, int size) {
        super(page, size);
    }

    public QueryMessage(int page, int size, Sort.Direction direction, String... properties) {
        super(page, size, direction, properties);
    }

    public QueryMessage(int page, int size, Sort sort) {
        super(page, size, sort);
    }

    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public Long getCreation() {
        return creation;
    }

    public void setCreation(Long creation) {
        this.creation = creation;
    }

    public String getCreationName() {
        return creationName;
    }

    public void setCreationName(String creationName) {
        this.creationName = creationName;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public MessageState getState() {
        return state;
    }

    public void setState(MessageState state) {
        this.state = state;
    }

    public static List<Predicate> initPredicates(QueryMessage queryMessage, Root<Message> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();

        predicates.add(QueryUtils.equals(root,cb,Message.ID,queryMessage.id));
        predicates.add(QueryUtils.equals(root,cb,Message.STATE,queryMessage.state));
        predicates.add(QueryUtils.equals(root,cb,Message.TYPE,queryMessage.type));
        predicates.add(QueryUtils.equals(root.get(Message.CREATION).get(Admin.USER).get(User.ID),cb,queryMessage.creation));
        predicates.add(QueryUtils.equals(root.get(Message.CREATION).get(Admin.USER).get(User.NAME),cb,queryMessage.creationName));
        predicates.add(QueryUtils.equals(root.get(Message.CREATION).get(Admin.USER).get(User.USERNAME),cb,queryMessage.account));

        predicates.add(QueryUtils.like(root,cb,Message.CONTEXT,queryMessage.context));
        predicates.add(QueryUtils.like(root,cb,Message.TITLE,queryMessage.title));

        predicates.add(QueryUtils.between(root, cb, Message.CREATE_TIME, queryMessage.getCreateTimeBegin(), queryMessage.getCreateTimeEnd()));

        predicates.removeIf(Objects::isNull);

        return predicates;
    }

    public static QueryMessage init(MessageState state){
        QueryMessage queryMessage = new QueryMessage();
        queryMessage.setState(state);
        return queryMessage;
    }
}
