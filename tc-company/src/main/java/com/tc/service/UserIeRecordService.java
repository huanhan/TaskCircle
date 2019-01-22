package com.tc.service;

import com.tc.db.entity.UserIeRecord;
import com.tc.dto.finance.QueryIE;
import org.springframework.data.domain.Page;

/**
 * 用户转账服务
 * @author Cyg
 */
public interface UserIeRecordService extends BasicService<UserIeRecord> {

    /**
     * 获取转账记录
     * @param queryIE 查询条件
     * @return
     */
    Page<UserIeRecord> findByQuery(QueryIE queryIE);
}
