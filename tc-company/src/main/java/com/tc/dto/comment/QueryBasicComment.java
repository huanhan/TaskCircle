package com.tc.dto.comment;

import com.tc.db.entity.Comment;
import com.tc.db.entity.CommentUser;
import com.tc.db.enums.CommentType;
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
 * 查询评论的公用内容
 * @author Cyg
 */
public class QueryBasicComment extends PageRequest {

    private Long id;
    private CommentType type;
    private String context;
    private Timestamp createTimeBegin;
    private Timestamp createTimeEnd;
    private Float numberBegin;
    private Float numberEnd;
    private Long creationId;
    private String creationName;
    private String creationAccount;


    public QueryBasicComment(int page, int size) {
        super(page, size);
    }

    public QueryBasicComment(int page, int size, Sort.Direction direction, String... properties) {
        super(page, size, direction, properties);
    }

    public QueryBasicComment(int page, int size, Sort sort) {
        super(page, size, sort);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CommentType getType() {
        return type;
    }

    public void setType(CommentType type) {
        this.type = type;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
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

    public Float getNumberBegin() {
        return numberBegin;
    }

    public void setNumberBegin(Float numberBegin) {
        this.numberBegin = numberBegin;
    }

    public Float getNumberEnd() {
        return numberEnd;
    }

    public void setNumberEnd(Float numberEnd) {
        this.numberEnd = numberEnd;
    }

    public Long getCreationId() {
        return creationId;
    }

    public void setCreationId(Long creationId) {
        this.creationId = creationId;
    }

    public String getCreationName() {
        return creationName;
    }

    public void setCreationName(String creationName) {
        this.creationName = creationName;
    }

    public String getCreationAccount() {
        return creationAccount;
    }

    public void setCreationAccount(String creationAccount) {
        this.creationAccount = creationAccount;
    }
}
