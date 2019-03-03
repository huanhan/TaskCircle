package com.tc.db.repository;

import com.tc.db.entity.HunterTask;
import com.tc.db.enums.HunterTaskState;
import com.tc.db.enums.MoneyType;
import com.tc.dto.audit.AuditContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;

/**
 * 评论仓库
 *
 * @author Cyg
 */
public interface HunterTaskRepository extends JpaRepository<HunterTask, String>, JpaSpecificationExecutor<HunterTask> {

    /**
     * 根据任务编号与状态查询
     *
     * @param taskId
     * @param hunterTaskState
     * @return
     */
    @Query(value = "select t from HunterTask t where t.taskId = :id and t.state = :state")
    List<HunterTask> findBy(@Param("id") String taskId, @Param("state") HunterTaskState hunterTaskState);

    /**
     * 根据任务编号与是否暂停获取猎刃任务
     *
     * @param taskId
     * @param isStop
     * @return
     */
    List<HunterTask> findByTaskIdAndStop(String taskId, Boolean isStop);


    /**
     * 获取指定任务的猎刃任务列表
     *
     * @param taskId
     * @return
     */
    List<HunterTask> findByTaskId(String taskId);

    /**
     * 根据编号列表获取
     *
     * @param ids
     * @return
     */
    List<HunterTask> findByIdIn(List<String> ids);


    /**
     * 根据猎刃任务编号与状态查询
     *
     * @param taskId
     * @param hunterTaskState
     * @return
     */
    @Query(value = "select t from HunterTask t where t.id = :id and t.state = :state")
    HunterTask findByIdAndState(@Param("id") String taskId, @Param("state") HunterTaskState hunterTaskState);

    /**
     * 根据任务编号与状态获取猎刃任务信息
     * @param taskId
     * @param hunterTaskState
     * @return
     */
    List<HunterTask> findByTaskIdAndState(@Param("id") String taskId, @Param("state") HunterTaskState hunterTaskState);

    /**
     * 更新任务状态，并将审核时间制空
     *
     * @param ids
     * @param state
     * @return
     */
    @Modifying
    @Query(value = "update HunterTask t set t.state = :state, t.adminAuditTime = NULL,t.adminId = NULL where t.id in (:ids)")
    int updateStateAndAdminAuditTime(@Param("ids") List<String> ids, @Param("state") HunterTaskState state);

    /**
     * 更新状态，并设置猎刃任务组的金额
     *
     * @param ids
     * @param state
     * @param money
     * @param type
     * @return
     */
    @Modifying
    @Query(value = "update HunterTask t set t.state = :state, t.money = :money, t.moneyType = :moneyType " +
            "where t.id in (:ids)")
    int updateState(@Param("ids") List<String> ids,
                    @Param("state") HunterTaskState state,
                    @Param("money") Float money,
                    @Param("moneyType") MoneyType type);

    /**
     * 更新任务状态
     *
     * @param state
     * @param id
     * @return
     */
    @Modifying
    @Query(value = "update HunterTask t set t.state = :state where t.id = :id")
    int updateState(@Param("id") String id, @Param("state") HunterTaskState state);

    /**
     * 更新状态，并设置猎刃任务的金额
     *
     * @param id
     * @param state
     * @param money
     * @param type
     * @return
     */
    @Modifying
    @Query(value = "update HunterTask t set t.state = :state, t.money = :money, t.moneyType = :moneyType " +
            "where t.id = :id")
    int updateState(@Param("id") String id,
                    @Param("state") HunterTaskState state,
                    @Param("money") Float money,
                    @Param("moneyType") MoneyType type);

    /**
     * 更新猎刃任务状态与审核时间
     *
     * @param id
     * @param state
     * @param timestamp
     * @return
     */
    @Modifying
    @Query(value = "update HunterTask t set t.state = :state, t.adminAuditTime = :time, t.adminId = :adminId where t.id = :id")
    int updateStateAndAdminAuditTime(@Param("id") String id, @Param("state") HunterTaskState state, @Param("time") Timestamp timestamp,@Param("adminId") Long adminId);

    /**
     * 更新猎刃任务状态与开始时间
     *
     * @param id
     * @param state
     * @param timestamp
     * @return
     */
    @Modifying
    @Query(value = "update HunterTask t set t.state = :state, t.beginTime = :time where t.id = :id")
    int updateStateAndBeginTime(@Param("id") String id, @Param("state") HunterTaskState state, @Param("time") Timestamp timestamp);

    /**
     * 更新猎刃任务状态与完成时间
     *
     * @param id
     * @param state
     * @param finishTime
     * @return
     */
    @Modifying
    @Query(value = "update HunterTask t set t.state = :state, t.finishTime = :time,t.userRejectCount = 0 where t.id = :id")
    int updateStateAndFinishTime(@Param("id") String id, @Param("state") HunterTaskState state, @Param("time") Timestamp finishTime);

    /**
     * 获取指定任务编号与包含指定状态的猎刃任务
     *
     * @param id     指定的任务编号
     * @param states 指定的状态
     * @return
     */
    List<HunterTask> findByTaskIdAndStateIn(String id, List<HunterTaskState> states);

    /**
     * 根据任务编号获取猎刃任务数量
     *
     * @param id
     * @return
     */
    int countByTaskId(String id);

    /**
     * 更新内容
     *
     * @param id
     * @param context
     * @return
     */
    @Modifying
    @Query(value = "update HunterTask  t set t.context = :context where t.id = :id")
    int updateContextById(@Param("id") String id, @Param("context") String context);


    /**
     * 设置状态与用户审核内容
     *
     * @param id
     * @param state
     * @param context
     * @return
     */
    @Modifying
    @Query(value = "update HunterTask  t set t.auditContext = :context,t.state = :state, t.userRejectCount = :iCount where t.id = :id")
    int updateStateAndAuditContext(@Param("id") String id, @Param("state") HunterTaskState state, @Param("context") String context, @Param("iCount") Integer count);

    /**
     * 设置状态与内容
     *
     * @param id
     * @param state
     * @param context
     * @param money
     * @param type
     * @return
     */
    @Modifying
    @Query(value = "update HunterTask  t set t.context = :context,t.state = :state, t.money = :money, t.moneyType = :moneyType " +
            "where t.id = :id")
    int updateStateAndContext(@Param("id") String id,
                              @Param("state") HunterTaskState state,
                              @Param("context") String context,
                              @Param("money") Float money,
                              @Param("moneyType") MoneyType type);

    /**
     * 更新猎刃任务状态，并重置用户拒绝猎刃放弃次数
     *
     * @param id
     * @param state
     * @return
     */
    @Modifying
    @Query(value = "update HunterTask  t set t.userRejectCount = 0,t.state = :state where t.id = :id")
    int updateStateAndResetURC(@Param("id") String id, @Param("state") HunterTaskState state);

    /**
     * 修改任务状态与猎刃提交审核的时间
     *
     * @param id
     * @param state
     * @param auditTime
     * @return
     */
    @Modifying
    @Query(value = "update HunterTask t set t.state = :state, t.auditTime = :time where t.id = :id")
    int updateStateAndAuditTime(@Param("id") String id, @Param("state") HunterTaskState state, @Param("time") Timestamp auditTime);

    /**
     * 修改猎刃任务为暂停(已弃用，建议使用stopAndStateTask)
     *
     * @param ids
     * @return
     */
    @Deprecated
    @Modifying
    @Query(value = "update HunterTask t set t.stop = true where t.id in (:ids)")
    int stopTask(@Param("ids") List<String> ids);

    /**
     * 修改猎刃任务为暂停且更改任务状态
     *
     * @param ids
     * @return
     */
    @Modifying
    @Query(value = "update HunterTask t set t.stop = true, t.oldState = t.state , t.state='WITH_HUNTER_NEGOTIATE' where t.id in (:ids)")
    int stopAndStateTask(@Param("ids") List<String> ids);

    /**
     * 设置猎刃任务状态，与拒绝用户需要的内容，并且拒绝次数自增1
     *
     * @param id
     * @param state
     * @param context
     * @return
     */
    @Modifying
    @Query(value = "update HunterTask  t set t.hunterRejectContext = :context,t.state = :state,t.hunterRejectCount = :iCount where t.id = :id")
    int updateStateAndHunterRejectContext(@Param("id") String id, @Param("state") HunterTaskState state, @Param("context") String context, @Param("iCount") Integer count);

    /**
     * 取消猎刃任务的暂停，并重置猎刃拒绝用户次数（已弃用,建议使用diStopAndState）
     *
     * @param strings
     * @return
     */
    @Deprecated
    @Modifying
    @Query(value = "update HunterTask t set t.hunterRejectCount = 0,t.stop = false where t.id in (:ids)")
    int diStop(@Param("ids") List<String> strings);

    /**
     * 取消猎刃任务的暂停，并重置猎刃拒绝用户次数
     *
     * @param strings
     * @return
     */
    @Modifying
    @Query(value = "update HunterTask t set t.hunterRejectCount = 0,t.stop = false,t.state = t.oldState where t.id in (:ids)")
    int diStopAndState(@Param("ids") List<String> strings);

    /**
     * 获取指定任务中，不包括指定状态列表的猎刃任务数量
     *
     * @param taskId
     * @param states
     * @return
     */
    @Query(value = "select count(t) from HunterTask t where t.taskId = :taskId and t.state in (:states)")
    int countByTaskIdAndHunterTaskStateNotIn(@Param("taskId") String taskId, @Param("states") List<HunterTaskState> states);

    /**
     * 根据任务编号与指定状态获取数量
     * @param taskId
     * @param state
     * @return
     */
    int countByTaskIdAndState(String taskId,HunterTaskState state);

    /**
     * 根据管理员编号获取该管理员的审核记录数目
     * @param id
     * @return
     */
    int countByAdminId(Long id);

    /**
     * 获取指定状态的猎刃任务数量
     * @param state
     * @return
     */
    int countByStateEquals(HunterTaskState state);
}
