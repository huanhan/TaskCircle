package com.tc.service.impl;

import com.google.gson.Gson;
import com.tc.db.entity.Message;
import com.tc.db.entity.User;
import com.tc.db.enums.MessageState;
import com.tc.db.repository.MessageRepository;
import com.tc.db.repository.UserRepository;
import com.tc.dto.LongIds;
import com.tc.dto.message.QueryMessage;
import com.tc.dto.user.QueryUser;
import com.tc.exception.DBException;
import com.tc.service.MessageService;
import com.tc.until.ListUtils;
import com.tc.until.StringResourceCenter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * 消息服务的实现
 * @author Cyg
 */
@Service
public class MessageServiceImpl extends AbstractBasicServiceImpl<Message> implements MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;



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
    public List<Message> findByQueryAndNotPage(QueryMessage queryMessage) {
        return messageRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = QueryMessage.initPredicates(queryMessage,root,query,cb);
            return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        });
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public boolean deleteCondition(Long id) {
        int count = messageRepository.deleteCondition(id);
        return count > 0;
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public boolean updateState(Long id, MessageState state) {
        int count = messageRepository.updateState(id,state);
        return count > 0;
    }

    @Transactional(rollbackFor = RuntimeException.class,readOnly = true)
    @Override
    public Message findOne(Long id) {
        Message message = messageRepository.findOne(id);
        if (message != null){
            if (!StringUtils.isEmpty(message.getLookCondition()) && message.getType() != null){
                long count = 0;
                try {
                    switch (message.getType()){
                        case CONDITION:
                            QueryUser queryUser;
                            queryUser = new Gson().fromJson(message.getLookCondition(),QueryUser.class);
                            count = userRepository.count((root, query, cb) -> {
                                List<Predicate> predicates = QueryUser.initPredicates(queryUser,root,query,cb);
                                return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
                            });
                            break;
                        case ALL:
                            count = userRepository.count();
                            break;
                        case APPOINT:
                            LongIds longIds = new Gson().fromJson(message.getLookCondition(),LongIds.class);
                            count = userRepository.countByIdIn(longIds.getIds());
                            break;
                        default:
                            break;
                    }
                }catch (Exception ignored){

                }
                message.setLookCount(count);
            }
        }
        return message;
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public Message save(Message message) {
        return messageRepository.saveAndFlush(message);
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public boolean deleteById(Long id) {
        try {
            messageRepository.delete(id);
        }catch (Exception e){
            throw new DBException(StringResourceCenter.DB_DELETE_ABNORMAL);
        }
        return true;
    }
}
