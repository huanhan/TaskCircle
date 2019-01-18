package com.tc.service.impl;

import com.tc.controller.AuditController;
import com.tc.db.entity.HunterTask;
import com.tc.db.entity.Task;
import com.tc.db.enums.HunterTaskState;
import com.tc.db.enums.TaskState;
import com.tc.db.repository.HunterTaskRepository;
import com.tc.db.repository.TaskRepository;
import com.tc.db.repository.UserRepository;
import com.tc.dto.task.QueryHunterTask;
import com.tc.exception.DBException;
import com.tc.exception.ValidException;
import com.tc.service.HunterTaskService;
import com.tc.until.ListUtils;
import com.tc.until.StringResourceCenter;
import com.tc.until.TimestampHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 猎刃任务服务的实现
 * @author Cyg
 */
@Service
public class HunterTaskServiceImpl extends AbstractBasicServiceImpl<HunterTask> implements HunterTaskService {

    @Autowired
    private HunterTaskRepository hunterTaskRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional(rollbackFor = RuntimeException.class,readOnly = true)
    @Override
    public Page<HunterTask> findByQueryHunterTask(QueryHunterTask queryHunterTask) {
        return hunterTaskRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = QueryHunterTask.initPredicatesByHunterTask(queryHunterTask,root,query,cb);
            return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        },queryHunterTask);
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public Boolean updateState(HunterTaskState state) {
        //获取任务状态为审核中的状态，并且审核时长超过设置的审核时长
        List<HunterTask> tasks = hunterTaskRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get(HunterTask.HUNTER_TASK_STATE),HunterTaskState.ADMIN_ADUIT));
            predicates.add(cb.lessThan(root.get(HunterTask.ADMIN_AUDIT_TIME),new Timestamp(System.currentTimeMillis() - AuditController.AUDIT_LONG)));
            return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        });
        if (ListUtils.isEmpty(tasks)){
            return true;
        }else {
            List<String> ids = HunterTask.toIds(tasks);
            int count = hunterTaskRepository.updateStateAndAdminAuditTime(ids,state);
            return count > 0;
        }
    }

    @Transactional(rollbackFor = RuntimeException.class,readOnly = true)
    @Override
    public HunterTask findByIdAndState(String id, HunterTaskState type) {
        return hunterTaskRepository.findByIdAndState(id,type);
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public Boolean updateState(String id, HunterTaskState state, Date date) {
        int count = hunterTaskRepository.updateStateAndAdminAuditTime(id,state,new Timestamp(date.getTime()));
        return count > 0;
    }

    @Transactional(rollbackFor = RuntimeException.class,readOnly = true)
    @Override
    public List<HunterTask> findByTaskId(String taskId) {
        return hunterTaskRepository.findByTaskId(taskId);
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public boolean acceptTask(Long id, String taskId) {

        int count;

        Task task = taskRepository.findOne(taskId);
        if (task == null){
            throw new DBException(StringResourceCenter.DB_QUERY_FAILED);
        }
        if (!task.getState().equals(TaskState.ISSUE)){
            throw new ValidException("任务已被人接取");
        }
        int numberPeople = hunterTaskRepository.countByTaskId(taskId);

        //判断任务是否被接满
        if (numberPeople + 1 >= task.getPeopleNumber()){
            //设置任务状态为禁止接取
            count = taskRepository.updateState(taskId,TaskState.FORBID_RECEIVE);

            if (count <= 0){
                throw new DBException("设置任务状态失败");
            }
        }

        //猎刃接任务（新增一条猎刃任务记录）
        HunterTask news = HunterTask.init(taskId,id);
        HunterTask result = hunterTaskRepository.save(news);
        if (result == null){
            throw new DBException(StringResourceCenter.DB_INSERT_FAILED);
        }

        //扣除押金
        count = userRepository.update(task.getCompensateMoney(),result.getHunterId());

        if (count <= 0){
            throw new DBException("扣除押金失败");
        }

        return true;
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public boolean beginTask(String taskId) {
        int count = hunterTaskRepository.updateStateAndBeginTime(taskId,HunterTaskState.BEGIN,TimestampHelper.today());
        return count > 0;
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public HunterTask update(String id, String context) {
        int count = hunterTaskRepository.updateContextById(id,context);
        if (count > 0){
            return hunterTaskRepository.findOne(id);
        }
        return null;
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public boolean updateState(String htId, HunterTaskState state) {
        int count = 0;
        switch (state){
            case AWAIT_USER_AUDIT:
                count = hunterTaskRepository.updateStateAndFinishTime(htId,state,TimestampHelper.today());
                break;
            default:
                break;
        }
        return count > 0;
    }
}
