package com.tc.service;

import com.tc.db.entity.HtsRecord;
import com.tc.db.entity.pk.HtsRecordPK;
import com.tc.dto.task.QueryHtsRecords;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 猎刃任务步骤更新记录服务
 * @author Cyg
 */
public interface HtsRecordService extends BasicService<HtsRecord> {
    /**
     * 根据猎刃任务号与步骤的编号获取对应的变更记录列表
     * @param tid
     * @param sid
     * @return
     */
    List<HtsRecord> findAll(String tid, Integer sid);

    /**
     * 根据联合主键获取详情
     * @param htsRecordPK
     * @return
     */
    HtsRecord findOne(HtsRecordPK htsRecordPK);

    /**
     * 根据查询条件获取
     * @param queryHtsRecords
     * @return
     */
    Page<HtsRecord> findByQuery(QueryHtsRecords queryHtsRecords);
}
