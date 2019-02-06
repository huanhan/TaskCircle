package com.tc.service.impl;

import com.tc.db.entity.CommentUser;
import com.tc.db.repository.CommentUserRepository;
import com.tc.dto.comment.QueryUserComment;
import com.tc.service.CommentUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.util.List;

/**
 * 评论用户服务的实现
 *
 * @author Cyg
 */
@Service
public class CommentUserServiceImpl extends AbstractBasicServiceImpl<CommentUser> implements CommentUserService {

    @Autowired
    private CommentUserRepository commentUserRepository;

    @Transactional(rollbackFor = RuntimeException.class, readOnly = true)
    @Override
    public Long countByUserId(Long userid) {
        return commentUserRepository.countByUserId(userid);
    }

    @Transactional(rollbackFor = RuntimeException.class, readOnly = true)
    @Override
    public Page<CommentUser> findByQuery(QueryUserComment queryUserComment) {
        return commentUserRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = QueryUserComment.initPredicates(queryUserComment, root, query, cb);
            return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        }, queryUserComment);
    }

    @Transactional(rollbackFor = RuntimeException.class, readOnly = true)
    @Override
    public CommentUser findOne(Long id) {
        return commentUserRepository.findOne(id);
    }

    @Override
    public CommentUser save(CommentUser commentUser) {
        return commentUserRepository.save(commentUser);
    }
}
