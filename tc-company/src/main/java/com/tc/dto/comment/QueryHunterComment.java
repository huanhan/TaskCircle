package com.tc.dto.comment;

import com.tc.db.entity.*;
import com.tc.until.QueryUtils;
import org.springframework.data.domain.Sort;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class QueryHunterComment extends QueryBasicComment{

    private Long hunterId;
    private String hunterName;
    private String account;
    private String hunterTaskId;

    public QueryHunterComment() {
        super(0,10);
    }

    public QueryHunterComment(int page, int size) {
        super(page, size);
    }

    public QueryHunterComment(int page, int size, Sort.Direction direction, String... properties) {
        super(page, size, direction, properties);
    }

    public QueryHunterComment(int page, int size, Sort sort) {
        super(page, size, sort);
    }

    public Long getHunterId() {
        return hunterId;
    }

    public void setHunterId(Long hunterId) {
        this.hunterId = hunterId;
    }

    public String getHunterName() {
        return hunterName;
    }

    public void setHunterName(String hunterName) {
        this.hunterName = hunterName;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getHunterTaskId() {
        return hunterTaskId;
    }

    public void setHunterTaskId(String hunterTaskId) {
        this.hunterTaskId = hunterTaskId;
    }


    public static List<Predicate> initPredicates(QueryHunterComment queryHunterComment, Root<CommentHunter> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();

        predicates.add(QueryUtils.equals(root.get(CommentHunter.COMMENT).get(Comment.ID),cb,queryHunterComment.getId()));
        predicates.add(QueryUtils.equals(root.get(CommentHunter.COMMENT).get(Comment.TYPE),cb,queryHunterComment.getType()));
        predicates.add(QueryUtils.equals(root.get(CommentHunter.COMMENT).get(Comment.CREATION_ID),cb,queryHunterComment.getCreationId()));
        predicates.add(QueryUtils.like(root.get(CommentHunter.COMMENT).get(Comment.CONTEXT),cb,queryHunterComment.getContext()));

        predicates.add(QueryUtils.between(root.get(CommentHunter.COMMENT).get(Comment.CREATE_TIME), cb, queryHunterComment.getCreateTimeBegin(), queryHunterComment.getCreateTimeEnd()));
        predicates.add(QueryUtils.between(root.get(CommentHunter.COMMENT).get(Comment.NUMBER), cb, queryHunterComment.getNumberBegin(), queryHunterComment.getNumberEnd()));

        predicates.add(QueryUtils.equals(root,cb,CommentHunter.HUNTER_ID,queryHunterComment.hunterId));
        predicates.add(QueryUtils.equals(root.get(CommentHunter.HUNTER).get(Hunter.USER).get(User.NAME),cb,queryHunterComment.hunterName));
        predicates.add(QueryUtils.equals(root.get(CommentHunter.HUNTER).get(Hunter.USER).get(User.USERNAME),cb,queryHunterComment.account));
        predicates.add(QueryUtils.equals(root,cb,CommentHunter.HUNTER_TASK_ID,queryHunterComment.hunterTaskId));

        predicates.removeIf(Objects::isNull);

        return predicates;
    }
}
