package com.tc.service.impl;

import com.tc.db.entity.CommentHunter;
import com.tc.db.repository.CommentHunterRepository;
import com.tc.dto.comment.QueryHunterComment;
import com.tc.dto.comment.QueryUserComment;
import com.tc.service.CommentHunterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.util.List;

/**
 * 评论猎刃服务的实现
 * @author Cyg
 */
@Service
public class CommentHunterServiceImpl extends AbstractBasicServiceImpl<CommentHunter> implements CommentHunterService {
    @Autowired
    private CommentHunterRepository commentHunterRepository;

    @Transactional(rollbackFor = RuntimeException.class,readOnly = true)
    @Override
    public Page<CommentHunter> findByQuery(QueryHunterComment queryHunterComment) {
        return commentHunterRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = QueryHunterComment.initPredicates(queryHunterComment,root,query,cb);
            return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        },queryHunterComment);
    }

    @Transactional(rollbackFor = RuntimeException.class,readOnly = true)
    @Override
    public CommentHunter findOne(Long id) {
        return commentHunterRepository.findOne(id);
    }

    @Override
    public CommentHunter save(CommentHunter commentHunter) {
        return commentHunterRepository.save(commentHunter);
    }
}
