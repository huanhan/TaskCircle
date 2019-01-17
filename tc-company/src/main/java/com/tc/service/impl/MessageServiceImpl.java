package com.tc.service.impl;

import com.tc.db.entity.Message;
import com.tc.db.repository.MessageRepository;
import com.tc.dto.message.QueryMessage;
import com.tc.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.util.List;

/**
 * 消息服务的实现
 * @author Cyg
 */
@Service
public class MessageServiceImpl extends AbstractBasicServiceImpl<Message> implements MessageService {

    @Autowired
    private MessageRepository messageRepository;



    @Transactional(rollbackFor = RuntimeException.class,readOnly = true)
    @Override
    public Page<Message> findByQuery(QueryMessage queryMessage) {
        return messageRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = QueryMessage.initPredicates(queryMessage,root,query,cb);
            return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        },queryMessage);
    }

    @Transactional(rollbackFor = RuntimeException.class,readOnly = true)
    @Override
    public Message findOne(Long id) {
        return messageRepository.findOne(id);
    }
}
