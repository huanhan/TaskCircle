package com.tc.service;

import com.tc.db.entity.HunterTaskStep;
import com.tc.db.entity.pk.HunterTaskStepPK;
import org.springframework.data.domain.Sort;

import java.util.List;

/**
 * 猎刃任务步骤服务接口，在BasicService中以有基本的服务，可以在这里额外添加
 *
 * @author Cyg
 */
public interface HunterTaskStepService extends BasicService<HunterTaskStep> {
    List<HunterTaskStep> findByHunterTaskId(String id,Sort sort);

    /**
     * 根据关联主键获取
     * @param hunterTaskStepPK
     * @return
     */
    HunterTaskStep findOne(HunterTaskStepPK hunterTaskStepPK);

    /**
     * 根据关联主键删除
     * @param hunterTaskStepPK
     * @return
     */
    boolean deleteById(HunterTaskStepPK hunterTaskStepPK);
}
