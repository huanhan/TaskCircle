package com.tc.service.impl;

import com.tc.controller.AuditController;
import com.tc.db.entity.*;
import com.tc.db.enums.HunterTaskState;
import com.tc.db.enums.MoneyType;
import com.tc.db.enums.TaskState;
import com.tc.db.repository.HunterTaskRepository;
import com.tc.db.repository.TaskRepository;
import com.tc.db.repository.UserIeRecordRepository;
import com.tc.db.repository.UserRepository;
import com.tc.dto.task.QueryHunterTask;
import com.tc.exception.DBException;
import com.tc.exception.ValidException;
import com.tc.service.HunterTaskService;
import com.tc.until.FloatHelper;
import com.tc.until.ListUtils;
import com.tc.until.StringResourceCenter;
import com.tc.until.TimestampHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 猎刃任务服务的实现
 *
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

    @Autowired
    private UserIeRecordRepository userIeRecordRepository;

    @Transactional(rollbackFor = RuntimeException.class, readOnly = true)
    @Override
    public Page<HunterTask> findByQueryHunterTask(QueryHunterTask queryHunterTask) {
        return hunterTaskRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = QueryHunterTask.initPredicatesByHunterTask(queryHunterTask, root, query, cb);
            return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        }, queryHunterTask);
    }

    @Transactional(rollbackFor = RuntimeException.class, readOnly = true)
    @Override
    public List<HunterTask> findByQueryHunterTaskAndNotPage(QueryHunterTask queryHunterTask) {
        return hunterTaskRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = QueryHunterTask.initPredicatesByHunterTask(queryHunterTask, root, query, cb);
            return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        }, queryHunterTask.getSort() == null ? new Sort(Sort.Direction.DESC, HunterTask.ACCEPT_TIME) : queryHunterTask.getSort());
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public Boolean updateState(HunterTaskState state) {
        //获取任务状态为审核中的状态，并且审核时长超过设置的审核时长
        List<HunterTask> tasks = hunterTaskRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get(HunterTask.HUNTER_TASK_STATE), HunterTaskState.ADMIN_AUDIT));
            predicates.add(cb.lessThan(root.get(HunterTask.ADMIN_AUDIT_TIME), new Timestamp(System.currentTimeMillis() - AuditController.AUDIT_LONG)));
            return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        });
        if (ListUtils.isEmpty(tasks)) {
            return true;
        } else {
            List<String> ids = HunterTask.toIds(tasks);
            int count = hunterTaskRepository.updateStateAndAdminAuditTime(ids, state);
            return count > 0;
        }
    }

    @Transactional(rollbackFor = RuntimeException.class, readOnly = true)
    @Override
    public HunterTask findByIdAndState(String id, HunterTaskState type) {
        return hunterTaskRepository.findByIdAndState(id, type);
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public Boolean updateState(String id, HunterTaskState state, Date date) {
        int count = hunterTaskRepository.updateStateAndAdminAuditTime(id, state, new Timestamp(date.getTime()));
        return count > 0;
    }

    @Transactional(rollbackFor = RuntimeException.class, readOnly = true)
    @Override
    public List<HunterTask> findByTaskId(String taskId) {
        return hunterTaskRepository.findByTaskId(taskId);
    }

    @Transactional(rollbackFor = RuntimeException.class, readOnly = true)
    @Override
    public HunterTask findOne(String htId) {
        return hunterTaskRepository.findOne(htId);
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public boolean acceptTask(Long id, String taskId) {

        int count;

        Task task = taskRepository.findOne(taskId);
        if (task == null) {
            throw new DBException(StringResourceCenter.DB_QUERY_FAILED);
        }
        if (!task.getState().equals(TaskState.ISSUE)) {
            throw new ValidException("任务已被人接取");
        }
        int numberPeople = hunterTaskRepository.countByTaskId(taskId);

        //判断任务是否被接满
        if (numberPeople + 1 >= task.getPeopleNumber()) {
            //设置任务状态为禁止接取
            count = taskRepository.updateState(taskId, TaskState.FORBID_RECEIVE);

            if (count <= 0) {
                throw new DBException("设置任务状态失败");
            }
        }


        //猎刃接任务（新增一条猎刃任务记录）
        HunterTask news = HunterTask.init(taskId, id);
        HunterTask result = hunterTaskRepository.save(news);
        if (result == null) {
            throw new DBException(StringResourceCenter.DB_INSERT_FAILED);
        }

        //判断用户账户余额
        User hunter = userRepository.findOne(result.getHunterId());
        if (hunter == null) {
            throw new DBException(StringResourceCenter.DB_QUERY_FAILED);
        }
        if (hunter.getMoney() < task.getCompensateMoney()) {
            throw new ValidException("用户账户余额不足，需要押金：" + task.getCompensateMoney() + "元");
        }

        //扣除押金
        Float dMoney = FloatHelper.sub(hunter.getMoney(), task.getCompensateMoney());
        count = userRepository.update(dMoney, result.getHunterId());

        if (count <= 0) {
            throw new DBException("扣除押金失败");
        }

        return true;
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public boolean beginTask(String taskId) {
        int count = hunterTaskRepository.updateStateAndBeginTime(taskId, HunterTaskState.BEGIN, TimestampHelper.today());
        return count > 0;
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public HunterTask update(String id, String context) {
        int count = hunterTaskRepository.updateContextById(id, context);
        if (count > 0) {
            return hunterTaskRepository.findOne(id);
        }
        return null;
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public boolean updateState(String htId, HunterTaskState state) {
        int count = 0;
        switch (state) {
            case AWAIT_USER_AUDIT:
                count = hunterTaskRepository.updateStateAndFinishTime(htId, state, TimestampHelper.today());
                break;
            case TASK_COMPLETE:
                count = hunterTaskRepository.updateStateAndResetURC(htId, state);
                break;
            default:
                break;
        }
        return count > 0;
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public boolean auditPassByUser(HunterTask hunterTask) {
        int count;
        //获取对应的任务信息
        Task task = hunterTask.getTask();
        //设置猎刃任务的状态为通过
        count = hunterTaskRepository.updateState(hunterTask.getId(), HunterTaskState.END_OK, task.getCompensateMoney(), MoneyType.INCOME);
        if (count <= 0) {
            throw new DBException("设置任务状态失败");
        }
        //获取猎刃信息
        Hunter hunter = hunterTask.getHunter();
        //为猎刃发赏金与退回押金
        Float yMoney = task.getCompensateMoney();
        Float sMoney = FloatHelper.divied(task.getMoney(), task.getPeopleNumber().floatValue());
        Float money = FloatHelper.addToBD(yMoney, sMoney).add(FloatHelper.toBig(hunter.getUser().getMoney())).floatValue();
        count = userRepository.update(money, hunterTask.getHunterId());
        if (count <= 0) {
            throw new DBException("设置金额失败");
        }
        //添加猎刃的转账记录
        UserIeRecord record = userIeRecordRepository.save(
                UserIeRecord.init(
                        task.getUserId(),
                        hunterTask.getHunterId(),
                        "来自任务（" + task.getName() + ")的赏金",
                        sMoney));
        if (record == null) {
            throw new DBException("添加转账记录失败");
        }
        //修改任务剩余赏金
        Float lMoney = FloatHelper.sub(task.getMoney(), sMoney);
        count = taskRepository.updateMoney(lMoney, task.getId());
        if (count <= 0) {
            throw new DBException("设置任务金额失败");
        }
        return true;
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public boolean auditNotPassByUser(String id, HunterTaskState state, String context) {
        int count = hunterTaskRepository.updateStateAndAuditContext(id, state, context);
        return count > 0;
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public boolean abandonTask(HunterTask hunterTask, String context) {

        int count;

        //获取对应的任务
        Task task = hunterTask.getTask();
        //获取对应的状态
        HunterTaskState hunterTaskState = hunterTask.getState();

        //判断用户是否也放弃了任务
        if (task.getState().equals(TaskState.ABANDON_COMMIT) && hunterTask.getStop()) {
            //设置猎刃任务状态为放弃任务
            count = hunterTaskRepository.updateState(hunterTask.getId(), HunterTaskState.TASK_ABANDON, 0F, MoneyType.IS_NULL);

            if (count <= 0) {
                throw new DBException("修改任务状态失败");
            }

            outMoneyToHunter(task, hunterTask);

            return true;
        }

        //不需要与用户协商的状态
        if (hunterTaskState.equals(HunterTaskState.RECEIVE)) {
            //当前状态是新接取状态

            //是否处于允许直接放弃的范围内
            if (TimestampHelper.differByMinute(TimestampHelper.today(), hunterTask.getAcceptTime()) <= task.getPermitAbandonMinute()) {

                //设置猎刃任务状态为放弃任务
                count = hunterTaskRepository.updateState(hunterTask.getId(), HunterTaskState.TASK_ABANDON, 0F, MoneyType.IS_NULL);

                if (count <= 0) {
                    throw new DBException("修改任务状态失败");
                }

                outMoneyToHunter(task, hunterTask);

                return true;
            }
        } else if (hunterTaskState.equals(HunterTaskState.NO_REWORK_HAVE_COMPENSATE) ||
                hunterTaskState.equals(HunterTaskState.ALLOW_REWORK_ABANDON_HAVE_COMPENSATE)) {
            //放弃需要补偿时

            //修改成结束未完成的状态，并保存放弃理由
            count = hunterTaskRepository.updateStateAndContext(hunterTask.getId(),
                    HunterTaskState.END_NO,
                    context,
                    task.getCompensateMoney(),
                    MoneyType.PAY);
            if (count <= 0) {
                throw new DBException("修改任务状态失败");
            }

            if (task.getCompensateMoney() > 0) {
                //获取对应用户信息
                User user = task.getUser();
                Float dMoney = FloatHelper.add(user.getMoney(), task.getCompensateMoney());
                //将押金给与用户补偿
                count = userRepository.update(dMoney, task.getUserId());
                if (count <= 0) {
                    throw new DBException("用户补偿失败");
                }
                //新增转账记录
                UserIeRecord userIeRecord = userIeRecordRepository.save(UserIeRecord.init(
                        hunterTask.getHunterId(),
                        task.getUserId(),
                        "来自（" + task.getName() + "）的猎刃放弃补偿",
                        task.getCompensateMoney()
                ));
                if (userIeRecord == null) {
                    throw new DBException("添加转账记录失败");
                }
            }

            //判断用户任务的状态是否需要重新发布任务
            if (task.getState().equals(TaskState.FORBID_RECEIVE)) {
                //重新发布用户任务
                count = taskRepository.updateStateAndIssueTime(task.getId(), TaskState.ISSUE, TimestampHelper.today());
                if (count <= 0) {
                    throw new DBException("修改任务状态失败");
                }
            }

            return true;
        } else if (hunterTaskState.equals(HunterTaskState.ALLOW_REWORK_ABANDON_NO_COMPENSATE) ||
                hunterTaskState.equals(HunterTaskState.NO_REWORK_NO_COMPENSATE)) {
            //放弃不需要补偿时

            //修改成结束未完成的状态，并保存放弃理由
            count = hunterTaskRepository.updateStateAndContext(hunterTask.getId(),
                    HunterTaskState.END_NO,
                    context,
                    0F,
                    MoneyType.IS_NULL);
            if (count <= 0) {
                throw new DBException("修改任务状态失败");
            }

            outMoneyToHunter(task, hunterTask);

            //判断用户任务的状态是否需要重新发布任务
            if (task.getState().equals(TaskState.FORBID_RECEIVE)) {
                //重新发布用户任务
                count = taskRepository.updateStateAndIssueTime(task.getId(), TaskState.ISSUE, TimestampHelper.today());
                if (count <= 0) {
                    throw new DBException("修改任务状态失败");
                }
            }

            return true;
        }

        //将放弃任务的申请提交与用户协商
        count = hunterTaskRepository.updateState(hunterTask.getId(), HunterTaskState.WITH_USER_NEGOTIATE);
        if (count <= 0) {
            throw new DBException("修改任务状态失败");
        }
        return false;
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public boolean toAdminAudit(String htId, HunterTaskState state) {
        int count = hunterTaskRepository.updateStateAndAuditTime(htId, state, TimestampHelper.today());
        return count > 0;
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public boolean diAdminAudit(String htId, HunterTaskState state) {
        int count = 0;
        if (state.equals(HunterTaskState.COMMIT_TO_ADMIN) || state.equals(HunterTaskState.WITH_ADMIN_NEGOTIATE)) {
            count = hunterTaskRepository.updateState(htId, HunterTaskState.USER_REPULSE);
        } else if (state.equals(HunterTaskState.COMMIT_ADMIN_AUDIT) || state.equals(HunterTaskState.ADMIN_AUDIT)) {
            HunterTask hunterTask = hunterTaskRepository.findOne(htId);
            Task task = hunterTask.getTask();
            //获取判断需要的状态
            boolean isRework = task.getTaskRework();
            boolean isCompensate = task.getCompensate();
            HunterTaskState hunterTaskState = HunterTaskState.getBy(isRework, isCompensate);
            count = hunterTaskRepository.updateState(htId, hunterTaskState);
        }
        return count > 0;
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public boolean abandonPassByUser(HunterTask hunterTask) {
        //获取对应的任务
        Task task = hunterTask.getTask();

        //将猎刃任务的状态设置成放弃
        int count = hunterTaskRepository.updateState(hunterTask.getId(), HunterTaskState.TASK_ABANDON, 0F, MoneyType.IS_NULL);
        if (count <= 0) {
            throw new DBException("修改任务状态失败");
        }

        outMoneyToHunter(task, hunterTask);

        //判断用户任务的状态是否需要重新发布任务
        if (task.getState().equals(TaskState.FORBID_RECEIVE)) {
            //重新发布用户任务
            count = taskRepository.updateStateAndIssueTime(task.getId(), TaskState.ISSUE, TimestampHelper.today());
            if (count <= 0) {
                throw new DBException("修改任务状态失败");
            }
        }

        return true;
    }

    @Transactional(rollbackFor = RuntimeException.class, readOnly = true)
    @Override
    public HunterTask findByTaskIsNotOk(String taskId, Long id) {

        //获取指定猎刃的执行任务列表
        List<HunterTask> hunterTasks = hunterTaskRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get(HunterTask.TASK_ID), taskId));
            predicates.add(cb.equal(root.get(HunterTask.HUNTER_ID), id));
            return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        });

        boolean isRemove = hunterTasks.removeIf(hunterTask -> HunterTaskState.isOk(hunterTask.getState()));
        if (isRemove && hunterTasks.size() == 1) {
            return hunterTasks.get(0);
        } else if (hunterTasks.size() == 1) {
            return hunterTasks.get(0);
        }
        return null;
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public boolean abandonPassByHunter(HunterTask hunterTask) {
        //获取对应的任务
        Task task = hunterTask.getTask();
        //设置猎人任务状态
        int count = hunterTaskRepository.updateState(hunterTask.getId(), HunterTaskState.TASK_BE_ABANDON, 0F, MoneyType.IS_NULL);
        if (count <= 0) {
            throw new DBException("修改任务状态失败");
        }
        this.outMoneyToHunter(task, hunterTask);
        return false;
    }

    @Transactional(rollbackFor = RuntimeException.class, readOnly = true)
    @Override
    public HunterTask findOne(String taskId, Long id) {
        return hunterTaskRepository.findOne((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get(HunterTask.TASK_ID), taskId));
            predicates.add(cb.equal(root.get(HunterTask.HUNTER_ID), id));
            predicates.add(cb.equal(root.get(HunterTask.IS_STOP), true));
            return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        });
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public boolean abandonNotPassByHunter(String id, String context) {
        int count = hunterTaskRepository.updateStateAndHunterRejectContext(id, HunterTaskState.HUNTER_REPULSE, context);
        return count > 0;
    }

    /**
     * 退还用户押金
     *
     * @param task
     * @param hunterTask
     */
    private void outMoneyToHunter(Task task, HunterTask hunterTask) {
        int count;
        if (task.getCompensateMoney() > 0) {
            //获取对应猎刃信息
            Hunter hunter = hunterTask.getHunter();
            Float dMoney = FloatHelper.add(hunter.getUser().getMoney(), task.getCompensateMoney());
            //退回猎刃押金
            count = userRepository.update(dMoney, hunterTask.getHunterId());
            if (count <= 0) {
                throw new DBException("退回押金失败");
            }
        }
    }
}
