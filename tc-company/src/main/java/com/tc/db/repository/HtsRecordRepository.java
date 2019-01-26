package com.tc.db.repository;

import com.tc.db.entity.HtsRecord;
import com.tc.db.entity.pk.HtsRecordPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


/**
 * 猎刃任务步骤更新记录
 * @author Cyg
 */
public interface HtsRecordRepository extends JpaRepository<HtsRecord, HtsRecordPK>,JpaSpecificationExecutor<HtsRecord> {
}
