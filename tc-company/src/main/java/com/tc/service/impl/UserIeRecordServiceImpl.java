package com.tc.service.impl;

import com.tc.db.entity.UserIeRecord;
import com.tc.db.repository.UserIeRecordRepository;
import com.tc.dto.finance.QueryIE;
import com.tc.service.UserIeRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.util.List;

/**
 * 用户收支记录服务的实现
 * @author Cyg
 */
@Service
public class UserIeRecordServiceImpl extends AbstractBasicServiceImpl<UserIeRecord> implements UserIeRecordService {

    @Autowired
    private UserIeRecordRepository userIeRecordRepository;


    @Transactional(rollbackFor = RuntimeException.class,readOnly = true)
    @Override
    public Page<UserIeRecord> findByQuery(QueryIE queryIE) {
        return userIeRecordRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = QueryIE.initPredicates(queryIE,root,query,cb);
            return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        },queryIE);
    }

    @Transactional(rollbackFor = RuntimeException.class,readOnly = true)
    @Override
    public UserIeRecord findOne(String id) {
        return userIeRecordRepository.findOne(id);
    }
}
