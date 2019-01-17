package com.tc.service.impl;

import com.tc.db.entity.Hunter;
import com.tc.db.repository.HunterRepository;
import com.tc.service.HunterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
