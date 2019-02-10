package com.tc.dto.comment;

import com.tc.db.entity.Comment;
import com.tc.db.enums.CommentType;
import com.tc.until.PageRequest;
import com.tc.until.QueryUtils;
import org.springframework.data.domain.Sort;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class QueryComment extends PageRequest {

    private Long creationId;
    private CommentType type;

    public QueryComment(int page, int size) {
        super(page, size);
    }

    public QueryComment(int page, int size, Sort.Direction direction, String... properties) {
        super(page, size, direction, properties);
    }

    public QueryComment(int page, int size, Sort sort) {
        super(page, size, sort);
    }

    public Long getCreationId() {
        return creationId;
    }

    public void setCreationId(Long creationId) {
        this.creationId = creationId;
    }

    public CommentType getType() {
        return type;
    }

    public void setType(CommentType type) {
        this.type = type;
    }

    public static List<Predicate> initPredicates(QueryComment queryComment, Root<Comment> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(QueryUtils.equals(root.get(Comment.CREATION_ID),cb,queryComment.getCreationId()));
        predicates.add(QueryUtils.equals(root.get(Comment.TYPE),cb,queryComment.getType()));
        predicates.removeIf(Objects::isNull);
        return predicates;
    }
}
