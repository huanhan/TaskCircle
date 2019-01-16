package com.tc.service.impl;

import com.tc.db.entity.CommentTask;
import com.tc.db.repository.CommentTaskRepository;
import com.tc.dto.comment.QueryTaskComment;
import com.tc.dto.comment.QueryUserComment;
import com.tc.service.CommentTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.List;

/**
 * 评论任务服务的实现
 * @author Cyg
 */
@Service
public class CommentTaskServiceImpl extends AbstractBasicServiceImpl<CommentTask> implements CommentTaskService {

    @Autowired
    private CommentTaskRepository commentTaskRepository;

    @Override
    public Page<CommentTask> findByQuery(QueryTaskComment queryTaskComment) {
        return commentTaskRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = QueryTaskComment.initPredicates(queryTaskComment,root,query,cb);
            return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        },queryTaskComment);
    }
}
