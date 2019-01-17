package com.tc.service.impl;

import com.tc.db.entity.HunterTaskStep;
import com.tc.db.repository.HunterTaskStepRepository;
import com.tc.service.HunterTaskStepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 猎刃任务步骤服务的实现
 *
 * @author Cyg
 */
@Service
public class HunterTaskStepServiceImpl extends AbstractBasicServiceImpl<HunterTaskStep> implements HunterTaskStepService {
    @Autowired
    private HunterTaskStepRepository hunterTaskStepRepository;

    public List<HunterTaskStep> findByHunterTaskId(String id,Sort sort) {
        return hunterTaskStepRepository.findByHunterTaskId(id,sort);
    }
}
