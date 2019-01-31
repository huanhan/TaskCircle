package com.tc.service.impl;

import com.tc.db.entity.UserHunterInterflow;
import com.tc.db.repository.UserHunterInterflowRepository;
import com.tc.dto.task.QueryTaskInterflow;
import com.tc.service.UserHunterInterflowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户猎刃交流服务的实现
 * @author Cyg
 */
@Service
public class UserHunterInterflowServiceImpl extends AbstractBasicServiceImpl<UserHunterInterflow> implements UserHunterInterflowService {

    @Autowired
    private UserHunterInterflowRepository userHunterInterflowRepository;

    @Autowired
    private EntityManager entityManager;


    @Transactional(rollbackFor = RuntimeException.class,readOnly = true)
    @Override
    public Page<UserHunterInterflow> findByQuery(QueryTaskInterflow queryTaskInterflow) {
        return userHunterInterflowRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = QueryTaskInterflow.initPredicatesByTask(queryTaskInterflow,root,query,cb);
            return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        },queryTaskInterflow);
    }

    @Transactional(rollbackFor = RuntimeException.class,readOnly = true)
    @Override
    public Page<UserHunterInterflow> findByQueryAndGroup(QueryTaskInterflow queryTaskInterflow) {
        return userHunterInterflowRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = QueryTaskInterflow.initPredicatesByTask(queryTaskInterflow,root,query,cb);
            List<Expression<?>> exceptions = new ArrayList<>();
            exceptions.add(root.get(UserHunterInterflow.TASK_ID));
            exceptions.add(root.get(UserHunterInterflow.HUNTER_ID));
            return query
                    .where(predicates.toArray(new Predicate[predicates.size()]))
                    .groupBy(exceptions).getRestriction();
        },queryTaskInterflow);
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public UserHunterInterflow save(UserHunterInterflow userHunterInterflow) {
        return userHunterInterflowRepository.save(userHunterInterflow);
    }
}
