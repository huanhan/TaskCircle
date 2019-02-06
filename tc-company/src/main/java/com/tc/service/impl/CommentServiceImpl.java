package com.tc.service.impl;

import com.tc.db.entity.Comment;
import com.tc.db.repository.CommentRepository;
import com.tc.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
