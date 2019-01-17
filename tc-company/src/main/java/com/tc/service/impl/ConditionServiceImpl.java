package com.tc.service.impl;

import com.tc.db.entity.Condition;
import com.tc.db.repository.ConditionRepository;
import com.tc.dto.condition.QueryCondition;
import com.tc.service.ConditionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.util.List;

/**
 * 条件服务的实现
 * @author Cyg
 */
@Service
public class ConditionServiceImpl extends AbstractBasicServiceImpl<Condition> implements ConditionService {

    @Autowired
    private ConditionRepository conditionRepository;



    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public Condition save(Condition condition) {
        return conditionRepository.save(condition);
    }

    @Transactional(rollbackFor = RuntimeException.class,readOnly = true)
    @Override
    public Page<Condition> findByQuery(QueryCondition queryCondition) {
        return conditionRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = QueryCondition.initPredicates(queryCondition,root,query,cb);
            return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        },queryCondition);
    }

    @Transactional(rollbackFor = RuntimeException.class,readOnly = true)
    @Override
    public Condition findOne(Long id) {
        return conditionRepository.findOne(id);
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public boolean deleteById(Long id) {
        try {
            conditionRepository.delete(id);
        }catch (Exception e){
            return false;
        }
        return true;
    }
}
