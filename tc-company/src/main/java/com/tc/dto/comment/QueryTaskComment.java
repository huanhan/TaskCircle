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

/**
 * 查询任务评论
 * @author Cyg
 */
public class QueryTaskComment extends QueryBasicComment {

    private String taskId;

    public QueryTaskComment(int page, int size) {
        super(page, size);
    }

    public QueryTaskComment(int page, int size, Sort.Direction direction, String... properties) {
        super(page, size, direction, properties);
    }

    public QueryTaskComment(int page, int size, Sort sort) {
        super(page, size, sort);
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public static List<Predicate> initPredicates(QueryTaskComment queryTaskComment, Root<CommentTask> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();

        predicates.add(QueryUtils.equals(root.get(CommentTask.COMMENT).get(Comment.ID),cb,queryTaskComment.getId()));
        predicates.add(QueryUtils.equals(root.get(CommentTask.COMMENT).get(Comment.TYPE),cb,queryTaskComment.getType()));
        predicates.add(QueryUtils.equals(root.get(CommentTask.COMMENT).get(Comment.CREATION_ID),cb,queryTaskComment.getCreationId()));
        predicates.add(QueryUtils.like(root.get(CommentTask.COMMENT).get(Comment.CONTEXT),cb,queryTaskComment.getContext()));

        predicates.add(QueryUtils.between(root.get(CommentTask.COMMENT).get(Comment.CREATE_TIME), cb, queryTaskComment.getCreateTimeBegin(), queryTaskComment.getCreateTimeEnd()));
        predicates.add(QueryUtils.between(root.get(CommentTask.COMMENT).get(Comment.NUMBER), cb, queryTaskComment.getNumberBegin(), queryTaskComment.getNumberEnd()));

        predicates.add(QueryUtils.equals(root,cb,CommentTask.TASK_ID,queryTaskComment.taskId));

        predicates.removeIf(Objects::isNull);

        return predicates;
    }
}
