package com.tc.service.impl;

import com.tc.db.entity.Hunter;
import com.tc.db.repository.HunterRepository;
import com.tc.dto.task.QueryHunterTask;
import com.tc.dto.user.hunter.QueryHunter;
import com.tc.service.HunterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.util.List;

/**
 * 猎刃服务的实现
 * @author Cyg
 */
@Service
public class HunterServiceImpl extends AbstractBasicServiceImpl<Hunter> implements HunterService {

    @Autowired
    private HunterRepository hunterRepository;

    @Transactional(rollbackFor = RuntimeException.class,readOnly = true)
    @Override
    public Hunter findOne(Long id) {
        return hunterRepository.findOne(id);
    }

    @Transactional(rollbackFor = RuntimeException.class,readOnly = true)
    @Override
    public Page<Hunter> findByQuery(QueryHunter queryHunter) {
        return hunterRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = QueryHunter.initPredicates(queryHunter,root,query,cb);
            return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        },queryHunter);
    }
}
