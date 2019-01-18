package com.tc.service.impl;

import com.tc.controller.AuditController;
import com.tc.db.entity.HunterTask;
import com.tc.db.entity.Task;
import com.tc.db.entity.User;
import com.tc.db.enums.HunterTaskState;
import com.tc.db.enums.TaskState;
import com.tc.db.repository.HunterTaskRepository;
import com.tc.db.repository.TaskRepository;
import com.tc.db.repository.UserRepository;
import com.tc.dto.task.QueryTask;
import com.tc.exception.DBException;
import com.tc.exception.ValidException;
import com.tc.service.TaskService;
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
 * 任务服务的实现
 * @author Cyg
 */
@Service
public class TaskServiceImpl extends AbstractBasicServiceImpl<Task> implements TaskService {


    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private HunterTaskRepository hunterTaskRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public Task save(Task task) {
        return taskRepository.saveAndFlush(task);
    }

    @Transactional(rollbackFor = RuntimeException.class,readOnly = true)
    @Override
    public Page<Task> findByQueryTask(QueryTask queryTask) {
        return taskRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = QueryTask.initPredicatesByTask(queryTask,root,query,cb);
            return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        },queryTask);
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public boolean updateState(String id, TaskState state) {
        int count = taskRepository.updateState(id,state);
        return count > 0;
    }

    @Transactional(rollbackFor = RuntimeException.class,readOnly = true)
    @Override
    public Task findByIdAndHunters(String id) {
        Task task = findOne(id);
        List<HunterTask> hunterTasks = hunterTaskRepository.findBy(id,HunterTaskState.HUNTER_REPULSE);
        task.setHunterTasks(hunterTasks);
        return task;
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public boolean updateState(String id, TaskState state, Date date) {
        int count = taskRepository.updateStateAndAdminAuditTime(id,state,new Timestamp(date.getTime()));
        return count > 0;
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public boolean updateState(TaskState state) {

        //获取任务状态为审核中的状态，并且审核时长超过设置的审核时长
        List<Task> tasks = taskRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get(Task.TASK_STATE),TaskState.AUDIT));
            predicates.add(cb.lessThan(root.get(Task.ADMIN_AUDIT_TIME),new Timestamp(System.currentTimeMillis() - AuditController.AUDIT_LONG)));
            return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        });
        if (ListUtils.isEmpty(tasks)){
            return true;
        }else {
            List<String> ids = Task.toIds(tasks);
            int count = taskRepository.updateState(ids,state);
            return count > 0;
        }
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public boolean commitAudit(String taskId, TaskState state) {

        int count = 0;

        if (state.equals(TaskState.NEW_CREATE) || state.equals(TaskState.HUNTER_REJECT)){
           //任务提交审核时一般只修改状态与提交审核的时间，不做其他额外的操作

           count = taskRepository.updateStateAndAuditTime(taskId,state,new Timestamp(System.currentTimeMillis()));

        }

        return count > 0;
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public int abandonTask(Long id, Task task) {
        int count = 0;

        //获取猎刃任务中接取状态的猎刃任务，并且从中选择接取时间小于允许放弃的时间
        List<HunterTask> hts = hunterTaskRepository.findBy(task.getId(),HunterTaskState.RECEIVE);
        List<String> ids = new ArrayList<>();
        if (hts != null){
            hts.forEach(hunterTask -> {
                if (TimestampHelper.differByMinute(TimestampHelper.today(),hunterTask.getAcceptTime()) <= task.getPermitAbandonMinute()){
                    ids.add(hunterTask.getId());
                }
            });
        }

        //先将可直接放弃的猎刃任务放弃掉
        if (!ListUtils.isEmpty(ids)) {

            //设置猎刃任务的状态为被放弃
            count = hunterTaskRepository.updateStateAndAdminAuditTime(ids, HunterTaskState.TASK_BE_ABANDON);

            if (count != ids.size()) {
                throw new DBException("修改猎刃任务状态错误");
            }

            //获取对应的猎刃编号
            List<Long> hids = hunterTaskRepository.findHunterById(ids);

            if (hids.size() != ids.size()) {
                throw new DBException("数据获取异常");
            }

            count = userRepository.update(hids, task.getCompensateMoney());

            if (count != ids.size()) {
                throw new DBException("退还猎刃押金失败");
            }

        }

        //获取该任务猎刃执行情况表中不允许放弃的猎刃任务
        hts = hunterTaskRepository.findByTaskIdAndStateIn(task.getId(),HunterTaskState.notAbandon());
        if (ListUtils.isEmpty(hts)){
            //说明用户已经可以直接放弃任务
            count = taskRepository.updateState(task.getId(),TaskState.ABANDON_OK);

            if (count <= 0){
                throw new DBException("更新任务状态失败");
            }

            Float money = task.getMoney();

            //更新用户金额（退还押金）
            count = userRepository.update(money,id);

            if (count <= 0){
                throw new DBException("退还用户押金失败");
            }

            count = 0;
        }else {
            //将任务状态设置为用户提交放弃申请状态，防止之后的人接取
            count = taskRepository.updateState(task.getId(),TaskState.ABANDON_COMMIT);

            if (count <= 0){
                throw new DBException("更新任务状态失败");
            }

            //说明由猎刃正在执行中，返回正在执行的猎刃数量
            count = hts.size();
        }

        return count;
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public Task updateAndUserMoney(Task task) {
        //获取发布该任务的用户信息
        User user = task.getUser();
        if (user.getMoney() < task.getMoney()){
            throw new ValidException("用户余额不足");
        }
        //保存新的任务信息，并设置状态为发布状态
        task.setState(TaskState.ISSUE);
        Task result = taskRepository.save(task);
        if (result == null){
            throw new DBException(StringResourceCenter.DB_UPDATE_ABNORMAL);
        }
        //扣除用户押金
        int count = userRepository.update(-task.getMoney(),user.getId());
        if (count <= 0){
            throw new DBException("扣除用户押金失败,任务发布失败");
        }
        return result;
    }

    @Transactional(rollbackFor = RuntimeException.class,readOnly = true)
    @Override
    public Task findOne(String id) {
        return taskRepository.findOne(id);
    }
}
