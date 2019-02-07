package com.tc.service.impl;

import com.tc.db.entity.Comment;
import com.tc.db.entity.CommentHunter;
import com.tc.db.repository.CommentRepository;
import com.tc.dto.comment.QueryComment;
import com.tc.dto.comment.QueryHunterComment;
import com.tc.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.util.List;

/**
 * 评论服务的实现
 * @author Cyg
 */
@Service
public class CommentServiceImpl extends AbstractBasicServiceImpl<Comment> implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }

    @Transactional(rollbackFor = RuntimeException.class,readOnly = true)
    @Override
    public Page<Comment> findByQuery(QueryComment queryComment) {
        return commentRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = QueryComment.initPredicates(queryComment,root,query,cb);
            return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        },queryComment);
    }
}
