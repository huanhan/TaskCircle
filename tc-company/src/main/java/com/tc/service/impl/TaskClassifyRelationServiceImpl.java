package com.tc.service.impl;

import com.tc.db.entity.TaskClassifyRelation;
import com.tc.db.repository.TaskClassifyRelationRepository;
import com.tc.dto.LongIds;
import com.tc.dto.StringIds;
import com.tc.service.TaskClassifyRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 任务分类关系服务的实现
 * @author Cyg
 */
@Service
public class TaskClassifyRelationServiceImpl extends AbstractBasicServiceImpl<TaskClassifyRelation> implements TaskClassifyRelationService {

    @Autowired
    private TaskClassifyRelationRepository taskClassifyRelationRepository;

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public boolean deleteByIds(LongIds ids) {
        int count = 0;//taskClassifyRelationRepository.deleteByTaskClassifyIdIsInAndTaskIdEquals(ids.getIds(),ids.getId());

        return count > 0;
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public boolean deleteByIds(StringIds ids) {
        int count = taskClassifyRelationRepository.deleteByTaskIdIsInAndTaskClassifyIdEquals(ids.getIds(),ids.getId());
        return count > 0;
    }
}
