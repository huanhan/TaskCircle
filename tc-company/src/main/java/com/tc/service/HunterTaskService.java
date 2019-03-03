package com.tc.service;

import com.tc.db.entity.HunterTask;
import com.tc.db.enums.CommentType;
import com.tc.db.enums.HunterTaskState;
import com.tc.dto.task.QueryHunterTask;
import org.springframework.data.domain.Page;

import java.util.Date;
import java.util.List;

/**
 * 猎刃任务服务接口，在BasicService中以有基本的服务，可以在这里额外添加
 * @author Cyg
 */
public interface HunterTaskService extends BasicService<HunterTask> {
    /**
     * 查询猎刃任务
     * @param queryHunterTask
     * @return
     */
    Page<HunterTask> findByQueryHunterTask(QueryHunterTask queryHunterTask);

    /**
     * 根据查询条件获取所有记录
     * @param queryHunterTask
     * @return
     */
    List<HunterTask> findByQueryHunterTaskAndNotPage(QueryHunterTask queryHunterTask);

    /**
     * 自动更新任务状态
     */
    Boolean updateState();

    /**
     * 根据状态和编号查询猎刃任务
     * @param id
     * @param type
     * @return
     */
    HunterTask findByIdAndState(String id, HunterTaskState type);

    /**
     * 锁定任务状态
     * @param id
     * @param state
     * @param date
     * @return
     */
    Boolean updateState(String id, HunterTaskState state, Date date,Long me);

    /**
     * 根据任务编号获取猎刃任务
     * @param taskId
     * @return
     */
    List<HunterTask> findByTaskId(String taskId);

    /**
     * 猎刃接任务
     * @param id
     * @param taskId
     * @return
     */
    String acceptTask(Long id, String taskId);

    /**
     * 猎刃开始任务
     * @param taskId
     * @return
     */
    boolean beginTask(String taskId);

    /**
     * 修改猎刃任务内容
     * @param id
     * @param context
     * @return
     */
    HunterTask update(String id, String context);

    /**
     * 更新状态，根据猎刃任务编号
     * @param htId
     * @param awaitUserAudit
     * @return
     */
    boolean updateState(String htId, HunterTaskState awaitUserAudit);

    /**
     * 用户审核猎刃的任务通过
     * @param hunterTask
     * @return
     */
    boolean auditPassByUser(HunterTask hunterTask);

    /**
     * 用户审核猎刃的任务不通过
     * @param id
     * @param state
     * @param context
     * @return
     */
    boolean auditNotPassByUser(String id, HunterTaskState state, String context,Integer iCount);

    /**
     * 猎刃放弃任务
     * @param hunterTask
     * @param context
     * @return
     */
    boolean abandonTask(HunterTask hunterTask, String context);

    //猎刃直接放弃任务
    boolean forceAbandonTask(HunterTask hunterTask, String context);

    /**
     * 将任务交给管理员审核
     * @param htId
     * @param state
     * @return
     */
    boolean toAdminAudit(String htId, HunterTaskState state);

    /**
     * 取消管理员的审核申请
     * @param htId
     * @param state
     * @return
     */
    boolean diAdminAudit(String htId, HunterTaskState state);

    /**
     * 猎刃的放弃申请被用户通过
     * @param hunterTask
     * @return
     */
    boolean abandonPassByUser(HunterTask hunterTask);

    /**
     * 获取指定猎刃任务中，指定的猎刃未完成的任务
     * @param taskId
     * @param id
     * @return
     */
    HunterTask findByTaskIsNotOk(String taskId, Long id);

    /**
     * 猎刃同意用户放弃
     * @param hunterTask
     * @return
     */
    boolean abandonPassByHunter(HunterTask hunterTask);

    /**
     * 根据指定任务编号与猎刃编号获取我的一个被暂停的任务
     * @param taskId
     * @param id
     * @return
     */
    HunterTask findOne(String taskId, Long id);

    /**
     * 猎刃不同意用户放弃任务
     * @param hunterTask
     * @param context
     * @param iCount
     * @return
     */
    boolean abandonNotPassByHunter(HunterTask hunterTask, String context,Integer iCount);

    HunterTask updateEvaState(String id, CommentType type);

    boolean abandonHunterTask(String hunterTaskId);

    boolean forceAbandonHunterTask(HunterTask hunterTaskId);

    /**
     * 获取指定任务与状态的猎刃任务信息
     * @param id
     * @param state
     * @return
     */
    List<HunterTask> findBy(String id, HunterTaskState state);

    /**
     * 根据指定管理员获取审核记录数
     * @param me
     * @return
     */
    Integer countByAdmin(Long me);
}
