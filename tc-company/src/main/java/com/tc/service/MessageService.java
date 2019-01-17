package com.tc.service;

import com.tc.db.entity.Message;
import com.tc.dto.message.QueryMessage;
import org.springframework.data.domain.Page;

/**
 * 审核服务接口，在BasicService中以有基本的服务，可以在这里额外添加
 * @author Cyg
 */
public interface MessageService extends BasicService<Message> {
    /**
     * 根据查询条件获取消息
     * @param queryMessage
     * @return
     */
    Page<Message> findByQuery(QueryMessage queryMessage);
}
