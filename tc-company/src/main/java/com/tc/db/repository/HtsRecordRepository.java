package com.tc.db.repository;

import com.tc.db.entity.HtsRecord;
import com.tc.db.entity.pk.HtsRecordPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;


/**
 * 猎刃任务步骤更新记录
 * @author Cyg
 */
public interface HtsRecordRepository extends JpaRepository<HtsRecord, HtsRecordPK>,JpaSpecificationExecutor<HtsRecord> {


    /**
     * 根据猎人任务编号与步骤编号获取步骤对应的变更记录
     * @param hunterTaskId
     * @param step
     * @return
     */
    List<HtsRecord> findByHunterTaskIdAndStep(String hunterTaskId,Integer step);

}
