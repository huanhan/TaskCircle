package com.tc.service;

import com.tc.db.entity.Message;
import com.tc.db.enums.MessageState;
import com.tc.dto.message.QueryMessage;
import org.springframework.data.domain.Page;

import java.util.List;

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

    /**
     * 根据查询条件获取消息
     * @param queryMessage
     * @return
     */
    List<Message> findByQueryAndNotPage(QueryMessage queryMessage);

    /**
     * 删除消息查看条件
     * @param id
     * @return
     */
    boolean deleteCondition(Long id);

    /**
     * 更新状态
     * @param id
     * @param state
     * @return
     */
    boolean updateState(Long id, MessageState state);

}
