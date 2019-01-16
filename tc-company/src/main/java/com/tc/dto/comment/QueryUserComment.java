package com.tc.dto.comment;

import com.tc.db.entity.Comment;
import com.tc.db.entity.CommentUser;
import com.tc.db.entity.User;
import com.tc.until.QueryUtils;
import org.springframework.data.domain.Sort;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 查询用户评论条件
 * @author Cyg
 */
public class QueryUserComment extends QueryBasicComment {


    private Long userId;
    private String userName;
    private String account;
    private String taskId;

    public QueryUserComment(int page, int size) {
        super(page, size);
    }

    public QueryUserComment(int page, int size, Sort.Direction direction, String... properties) {
        super(page, size, direction, properties);
    }

    public QueryUserComment(int page, int size, Sort sort) {
        super(page, size, sort);
    }


    public static List<Predicate> initPredicates(QueryUserComment queryUserComment, Root<CommentUser> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();

        predicates.add(QueryUtils.equals(root.get(CommentUser.COMMENT).get(Comment.ID),cb,queryUserComment.getId()));
        predicates.add(QueryUtils.equals(root.get(CommentUser.COMMENT).get(Comment.TYPE),cb,queryUserComment.getType()));
        predicates.add(QueryUtils.equals(root.get(CommentUser.COMMENT).get(Comment.CREATION_ID),cb,queryUserComment.getCreationId()));
        predicates.add(QueryUtils.like(root.get(CommentUser.COMMENT).get(Comment.CONTEXT),cb,queryUserComment.getContext()));

        predicates.add(QueryUtils.between(root.get(CommentUser.COMMENT).get(Comment.CREATE_TIME), cb, queryUserComment.getCreateTimeBegin(), queryUserComment.getCreateTimeEnd()));
        predicates.add(QueryUtils.between(root.get(CommentUser.COMMENT).get(Comment.NUMBET), cb, queryUserComment.getNumberBegin(), queryUserComment.getNumberEnd()));

        predicates.add(QueryUtils.equals(root,cb,CommentUser.USER_ID,queryUserComment.userId));
        predicates.add(QueryUtils.equals(root.get(CommentUser.USER).get(User.NAME),cb,queryUserComment.userName));
        predicates.add(QueryUtils.equals(root.get(CommentUser.USER).get(User.USERNAME),cb,queryUserComment.account));
        predicates.add(QueryUtils.equals(root,cb,CommentUser.TASK_ID,queryUserComment.taskId));

        predicates.removeIf(Objects::isNull);

        return predicates;
    }
}
