package com.tc.service.impl;

import com.tc.controller.AppTaskController;
import com.tc.controller.AuditController;
import com.tc.db.entity.HunterTask;
import com.tc.db.entity.Task;
import com.tc.db.entity.User;
import com.tc.db.enums.HunterTaskState;
import com.tc.db.enums.MoneyType;
import com.tc.db.enums.TaskState;
import com.tc.db.repository.*;
import com.tc.dto.TimeScope;
import com.tc.dto.task.QueryTask;
import com.tc.exception.DBException;
import com.tc.exception.ValidException;
import com.tc.service.TaskService;
import com.tc.until.FloatHelper;
import com.tc.until.ListUtils;
import com.tc.until.StringResourceCenter;
import com.tc.until.TimestampHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 任务服务的实现
 *
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

    @Autowired
    private HunterRepository hunterRepository;

    @Autowired
    private TaskClassifyRelationRepository taskClassifyRelationRepository;

    @Autowired
    private TaskStepRepository taskStepRepository;


    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public Task save(Task task) {
        return taskRepository.saveAndFlush(task);
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public Task modify(Task task) {
        //删除分类关系
        taskClassifyRelationRepository.deleteByTaskId(task.getId());
        //删除步骤
        taskStepRepository.deleteByTaskId(task.getId());
        //保存
        return taskRepository.saveAndFlush(task);
    }

    @Transactional(rollbackFor = RuntimeException.class, readOnly = true)
    @Override
    public Page<Task> findByQueryTask(QueryTask queryTask) {
        return taskRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = QueryTask.initPredicatesByTask(queryTask, root, query, cb);
            return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        }, queryTask);
    }

    @Transactional(rollbackFor = RuntimeException.class, readOnly = true)
    @Override
    public List<Task> findByQueryTaskAndNotPage(QueryTask queryTask) {
        return taskRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = QueryTask.initPredicatesByTask(queryTask, root, query, cb);
            return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        });
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public boolean updateState(String id, TaskState state) {
        int count;
        switch (state) {
            case ISSUE:
                count = taskRepository.updateStateAndIssueTime(id, state, TimestampHelper.today());
                break;
            default:
                count = taskRepository.updateState(id, state);
                break;
        }
        return count > 0;
    }

    @Transactional(rollbackFor = RuntimeException.class, readOnly = true)
    @Override
    public Task findByIdAndHunters(String id) {
        Task task = findOne(id);
        List<HunterTask> hunterTasks = hunterTaskRepository.findBy(id, HunterTaskState.HUNTER_REPULSE);
        task.setHunterTasks(hunterTasks);
        return task;
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public boolean updateState(String id, TaskState state, Date date) {
        int count = taskRepository.updateStateAndAdminAuditTime(id, state, new Timestamp(date.getTime()));
        return count > 0;
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public boolean updateState(TaskState state) {

        //获取任务状态为审核中的状态，并且审核时长超过设置的审核时长
        List<Task> tasks = taskRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get(Task.TASK_STATE), TaskState.AUDIT));
            predicates.add(cb.lessThan(root.get(Task.ADMIN_AUDIT_TIME), new Timestamp(System.currentTimeMillis() - AuditController.AUDIT_LONG)));
            return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        });
        if (ListUtils.isEmpty(tasks)) {
            return true;
        } else {
            List<String> ids = Task.toIds(tasks);
            int count = taskRepository.updateState(ids, state);
            return count > 0;
        }
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public boolean commitAudit(String taskId, TaskState state) {

        int count = 0;

        if (state.equals(TaskState.NEW_CREATE) || state.equals(TaskState.HUNTER_REJECT)) {
            //任务提交审核时一般只修改状态与提交审核的时间，不做其他额外的操作
            TaskState setState = state.equals(TaskState.NEW_CREATE) ? TaskState.AWAIT_AUDIT : TaskState.COMMIT_AUDIT;
            count = taskRepository.updateStateAndAuditTime(taskId, setState, new Timestamp(System.currentTimeMillis()));

        }

        return count > 0;
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public boolean diCommitAudit(String taskId, TaskState state) {
        int count = 0;
        if (state.equals(TaskState.ADMIN_NEGOTIATE) || state.equals(TaskState.COMMIT_AUDIT)) {
            count = taskRepository.updateState(taskId, TaskState.HUNTER_REJECT);
        } else if (state.equals(TaskState.AUDIT) || state.equals(TaskState.AWAIT_AUDIT)) {
            count = taskRepository.updateState(taskId, TaskState.NEW_CREATE);
        }
        return count > 0;
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public boolean diAbandonTask(Long id, Task task) {
        int count = 0;
        //设置任务状态为撤回
        count = taskRepository.updateState(task.getId(), TaskState.OUT);
        if (count <= 0) {
            throw new DBException("任务状态修改失败");
        }
        //获取所有暂停的猎刃任务列表
        List<HunterTask> hunterTasks = hunterTaskRepository.findByTaskIdAndStop(task.getId(), true);
        if (ListUtils.isEmpty(hunterTasks)) {
            throw new DBException(StringResourceCenter.DB_QUERY_FAILED);
        }
        //将猎刃任务取消暂停
        count = hunterTaskRepository.diStop(HunterTask.toIds(hunterTasks));
        if (count != hunterTasks.size()) {
            throw new DBException("取消猎刃任务暂停失败");
        }
        return true;
    }

    @Override
    public boolean adminUpdateState(String id, TaskState state) {
        return false;
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public int abandonTask(Long id, Task task) {
        int count = 0;

        //获取猎刃任务中接取状态的猎刃任务，并且从中选择接取时间小于允许放弃的时间
        List<HunterTask> hts = hunterTaskRepository.findByTaskIdAndStateIn(task.getId(), HunterTaskState.isAbandon());
        List<String> ids = new ArrayList<>();
        if (hts != null) {
            hts.forEach(hunterTask -> {
                if (hunterTask.getState().equals(HunterTaskState.RECEIVE)) {
                    if (TimestampHelper.differByMinute(TimestampHelper.today(), hunterTask.getAcceptTime()) <= task.getPermitAbandonMinute()) {
                        ids.add(hunterTask.getId());
                    }
                } else {
                    ids.add(hunterTask.getId());
                }
            });
        }

        //先将可直接放弃的猎刃任务放弃掉
        if (!ListUtils.isEmpty(ids)) {

            //设置猎刃任务的状态为被放弃
            count = hunterTaskRepository.updateState(ids, HunterTaskState.TASK_BE_ABANDON, 0F, MoneyType.IS_NULL);

            if (count != ids.size()) {
                throw new DBException("修改猎刃任务状态错误");
            }

            //获取对应的猎刃编号

            List<HunterTask> hunterTasks = hunterTaskRepository.findByIdIn(ids);

            if (hunterTasks.size() != ids.size()) {
                throw new DBException("数据获取异常");
            }

            count = 0;
            for (HunterTask hunterTask :
                    hunterTasks) {
                Float dMoney = FloatHelper.add(hunterTask.getHunter().getUser().getMoney(), task.getCompensateMoney());
                userRepository.update(dMoney, hunterTask.getHunterId());
                count++;
            }

            if (count != ids.size()) {
                throw new DBException("退还猎刃押金失败");
            }

        }

        //获取该任务猎刃执行情况表中不允许放弃的猎刃任务
        hts = hunterTaskRepository.findByTaskIdAndStateIn(task.getId(), HunterTaskState.notAbandon());
        if (ListUtils.isEmpty(hts)) {
            //说明用户已经可以直接放弃任务
            count = taskRepository.updateState(task.getId(), TaskState.ABANDON_OK);

            if (count <= 0) {
                throw new DBException("更新任务状态失败");
            }

            Float money = task.getMoney();
            //获取对应用户信息
            User user = task.getUser();
            Float dMoney = FloatHelper.add(user.getMoney(), money);
            //更新用户金额（退还押金）
            count = userRepository.update(dMoney, id);

            if (count <= 0) {
                throw new DBException("退还用户押金失败");
            }

            count = 0;
        } else {

            //判断是否允许用户放弃任务
            if (hts.size() <= AppTaskController.USER_ABANDON_NUMBER) {
                //将任务状态设置为用户提交放弃申请状态，防止之后的人接取
                count = taskRepository.updateState(task.getId(), TaskState.ABANDON_COMMIT);

                if (count <= 0) {
                    throw new DBException("更新任务状态失败");
                }


                //将猎刃的任务状态置为暂停状态，阻止猎刃继续进行任务
                count = hunterTaskRepository.stopTask(HunterTask.toIds(hts));
                if (count != hts.size()) {
                    throw new DBException("暂停猎刃任务失败");
                }
            } else {

                if (!task.getState().equals(TaskState.OUT)) {
                    //将任务撤回，不能放弃
                    count = taskRepository.updateState(task.getId(), TaskState.OUT);

                    if (count <= 0) {
                        throw new DBException("更新任务状态失败");
                    }
                }

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
        if (user.getMoney() < task.getMoney()) {
            throw new ValidException("用户余额不足");
        }

        //保存新的任务信息
        task.setState(TaskState.ISSUE);
        Task result = taskRepository.save(task);
        if (result == null) {
            throw new DBException(StringResourceCenter.DB_UPDATE_ABNORMAL);
        }
        Float dMoney = FloatHelper.sub(user.getMoney(), task.getMoney());
        //扣除用户押金
        int count = userRepository.update(dMoney, user.getId());
        if (count <= 0) {
            throw new DBException("扣除用户押金失败,任务发布失败");
        }
        return result;
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public boolean outTask(Task task) {
        int count;

        //获取满足放弃的条件的猎刃任务列表（任务编号，任务状态，允许放弃的分钟数）
        List<HunterTask> queryHts = hunterTaskRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get(HunterTask.TASK_ID), task.getId()));
            predicates.add(cb.equal(root.get(HunterTask.HUNTER_TASK_STATE), HunterTaskState.RECEIVE));
            predicates.add(cb.lessThan(root.get(HunterTask.ACCEPT_TIME), TimestampHelper.addMinuteByToday(task.getPermitAbandonMinute())));
            return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        });
        if (!ListUtils.isEmpty(queryHts)) {
            //获取猎刃任务编号，用来修改状态
            List<String> ids = HunterTask.toIds(queryHts);
            //设置猎刃任务的状态为被放弃
            count = hunterTaskRepository.updateState(ids, HunterTaskState.TASK_BE_ABANDON, 0F, MoneyType.IS_NULL);
            if (count != ids.size()) {
                throw new DBException("修改猎刃任务状态错误");
            }
            //退回猎刃押金
            count = 0;
            for (HunterTask hunterTask :
                    queryHts) {
                Float dMoney = FloatHelper.add(hunterTask.getHunter().getUser().getMoney(), task.getCompensateMoney());
                userRepository.update(dMoney, hunterTask.getHunterId());
                count++;
            }
            if (count != ids.size()) {
                throw new DBException("退还猎刃押金失败");
            }
        }
        //修改任务状态为撤回
        count = taskRepository.updateState(task.getId(), TaskState.OUT);
        if (count <= 0) {
            throw new DBException("任务撤回失败");
        }
        return true;

    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public boolean taskIsSuccess(String taskId) {
        Task task = taskRepository.findOne(taskId);
        if (task == null) {
            throw new DBException(StringResourceCenter.DB_QUERY_FAILED);
        }
        //根据猎刃任务已经完成的人数判断任务成功与否
        long peopleNumber = hunterTaskRepository.count((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get(HunterTask.TASK_ID), taskId));
            predicates.add(cb.equal(root.get(HunterTask.HUNTER_TASK_STATE), HunterTaskState.END_OK));
            return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        });
        if (!task.getPeopleNumber().equals(peopleNumber)) {
            return false;
        }
        int count = taskRepository.updateState(taskId, TaskState.FINISH);
        if (count <= 0) {
            throw new DBException("修改任务状态失败");
        }
        return true;
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public boolean taskIsReject(String id) {
        Task task = taskRepository.findOne(id);
        if (task == null) {
            throw new DBException(StringResourceCenter.DB_QUERY_FAILED);
        }
        //获取除了已拒绝外的剩下猎刃任务数
        int count = hunterTaskRepository.countByTaskIdAndHunterTaskStateNotIn(id, HunterTaskState.notAbandonState());
        if (count > 0) {
            return false;
        }
        //如果任务数等于0
        count = taskRepository.updateState(id, TaskState.HUNTER_REJECT);
        if (count <= 0) {
            throw new DBException(StringResourceCenter.DB_UPDATE_ABNORMAL);
        }
        return true;
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public boolean hasAbandon(Task task) {
        //判断是否还有猎刃在执行任务
        List<HunterTask> hts = hunterTaskRepository.findByTaskIdAndStateIn(task.getId(), HunterTaskState.notAbandon());
        if (!ListUtils.isEmpty(hts)) {
            return false;
        }
        //放弃任务
        int count = taskRepository.updateState(task.getId(), TaskState.ABANDON_OK);
        if (count <= 0) {
            throw new DBException("修改任务状态失败");
        }
        //获取发布该任务的用户信息
        User user = task.getUser();
        Float dMoney = FloatHelper.sub(user.getMoney(), task.getMoney());
        count = userRepository.update(dMoney, task.getUserId());
        if (count <= 0) {
            throw new DBException("退还用户押金失败");
        }
        return true;
    }


    @Transactional(rollbackFor = RuntimeException.class, readOnly = true)
    @Override
    public Task findOne(String id) {
        return taskRepository.findOne(id);
    }

    @Transactional(rollbackFor = RuntimeException.class, readOnly = true)
    @Override
    public Page<Task> findCashPledge(Long id, TimeScope scope) {
        scope.setId(id);
        return taskRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = QueryTask.initPredicatesBy(scope, root, query, cb);
            return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        }, scope);
    }

    @Override
    public Page<Task> search(String key, Pageable pageable) {
        return taskRepository.searchTask(key, pageable);
    }

    @Override
    public List<Task> taskByDistance(Double lat, Double log ) {
        return taskRepository.taskByDistance(lat, log );
    }
}
