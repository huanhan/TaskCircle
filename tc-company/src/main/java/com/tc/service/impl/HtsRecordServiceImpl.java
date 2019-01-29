package com.tc.service.impl;

import com.tc.db.entity.HtsRecord;
import com.tc.db.entity.pk.HtsRecordPK;
import com.tc.db.repository.HtsRecordRepository;
import com.tc.dto.task.QueryHtsRecords;
import com.tc.service.HtsRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.util.List;

/**
 * 猎刃任务步骤更新记录服务的实现
 *
 * @author Cyg
 */
@Service
public class HtsRecordServiceImpl extends AbstractBasicServiceImpl<HtsRecord> implements HtsRecordService {

    @Autowired
    private HtsRecordRepository htsRecordRepository;

    @Transactional(rollbackFor = RuntimeException.class, readOnly = true)
    @Override
    public List<HtsRecord> findAll(String tid, Integer sid) {
        return htsRecordRepository.findByHunterTaskIdAndStep(tid, sid);
    }

    @Transactional(rollbackFor = RuntimeException.class, readOnly = true)
    @Override
    public HtsRecord findOne(HtsRecordPK htsRecordPK) {
        return htsRecordRepository.findOne(htsRecordPK);
    }

    @Transactional(rollbackFor = RuntimeException.class, readOnly = true)
    @Override
    public Page<HtsRecord> findByQuery(QueryHtsRecords queryHtsRecords) {
        return htsRecordRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = QueryHtsRecords.initPredicates(queryHtsRecords,root,query,cb);
            return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        },queryHtsRecords);

    }

    @Transactional(rollbackFor = RuntimeException.class, readOnly = true)
    @Override
    public HtsRecord save(HtsRecord htsRecord) {
        return htsRecordRepository.save(htsRecord);
    }
}
