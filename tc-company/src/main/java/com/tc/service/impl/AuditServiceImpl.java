package com.tc.service.impl;

import com.tc.controller.FinanceController;
import com.tc.db.entity.*;
import com.tc.db.enums.*;
import com.tc.db.repository.*;
import com.tc.dto.audit.QueryAudit;
import com.tc.dto.trans.Trans;
import com.tc.exception.DBException;
import com.tc.exception.ValidException;
import com.tc.service.AuditService;
import com.tc.until.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * 审核服务的实现
 *
 * @author Cyg
 */
@Service
public class AuditServiceImpl extends AbstractBasicServiceImpl<Audit> implements AuditService {

    @Autowired
    private AuditRepository auditRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HunterRepository hunterRepository;

    @Autowired
    private UserWithdrawRepository userWithdrawRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private HunterTaskRepository hunterTaskRepository;

    @Autowired
    private UserIeRecordRepository userIeRecordRepository;

    @Transactional(rollbackFor = RuntimeException.class, readOnly = true)
    @Override
    public Page<Audit> findByQueryAudit(QueryAudit queryAudit) {
        return auditRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = QueryAudit.initPredicates(queryAudit, root, query, cb);
            return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        }, queryAudit);
    }

    @Transactional(rollbackFor = RuntimeException.class, readOnly = true)
    @Override
    public Page<Audit> findByQueryAndUser(QueryAudit queryAudit) {
        return auditRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = QueryAudit.initPredicatesByUser(queryAudit, root, query, cb);
            return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        }, queryAudit);
    }

    @Transactional(rollbackFor = RuntimeException.class, readOnly = true)
    @Override
    public List<Trans> findByAudit() {
        List<Trans> trans = new ArrayList<>();
        int count = 0;
        count = taskRepository.countByStateEquals(TaskState.COMMIT_AUDIT);
        trans.add(new Trans(AuditType.USER_FAILURE_TASK.name(),count + ""));
        count = taskRepository.countByStateEquals(TaskState.AWAIT_AUDIT);
        trans.add(new Trans(AuditType.TASK.name(),count + ""));
        count = hunterTaskRepository.countByStateEquals(HunterTaskState.COMMIT_TO_ADMIN);
        trans.add(new Trans(AuditType.HUNTER_FAILURE_TASK,count + ""));
        count = hunterTaskRepository.countByStateEquals(HunterTaskState.COMMIT_ADMIN_AUDIT);
        trans.add(new Trans(AuditType.HUNTER_OK_TASK,count + ""));
        count = userWithdrawRepository.countByStateEquals(WithdrawState.AUDIT);
        trans.add(new Trans(AuditType.WITHDRAW,count + ""));
        count = userWithdrawRepository.countByStateEquals(WithdrawState.PAY_AUDIT);
        trans.add(new Trans(AuditType.PAY,count + ""));
        count = userRepository.countByStateEquals(UserState.AUDIT_HUNTER);
        trans.add(new Trans(AuditType.HUNTER,count + ""));
        return trans;
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public Audit save(Audit audit) {
        Audit result = auditRepository.save(audit);
        if (result != null) {
            int count = 0;
            final Task task = (result.getAuditTask() == null ? null : taskRepository.findOne(result.getAuditTask().getTaskId()));
            final UserWithdraw userWithdraw = (result.getAuditWithdraw() == null ? null : userWithdrawRepository.findOne(result.getAuditWithdraw().getWithdrawId()));
            final HunterTask hunterTask = (result.getAuditHunterTask() == null ? null : hunterTaskRepository.findOne(result.getAuditHunterTask().getHunterTaskId()));
            switch (audit.getType()) {
                case HUNTER:


                    if (result.getAuditHunter() == null) {
                        throw new DBException(StringResourceCenter.DB_QUERY_FAILED);
                    }

                    Long userId = result.getAuditHunter().getUserId();

                    UserCategory userCategory = UserCategory.NORMAL;
                    //如果审核通过，设置用户为猎刃
                    if (audit.getResult().equals(AuditState.PASS)) {
                        userCategory = UserCategory.HUNTER;

                        //添加猎刃
                        hunterRepository.save(new Hunter(userId));

                    }
                    //不管是否通过，都会将人物状态修改为正常
                    count = userRepository.update(UserState.NORMAL, userCategory, audit.getAuditHunter().getUserId());
                    break;
                case WITHDRAW:

                    //获取用户提现记录不能为空
                    if (userWithdraw == null) {
                        throw new DBException(StringResourceCenter.DB_QUERY_FAILED);
                    }

                    //根据管理员的设置变动用户真实提现金额
                    Float wMoney = audit.getAuditWithdraw().getAdminMoney();
                    Float realityMoney = FloatHelper.subToBD(1f, FinanceController.VACUATE).multiply(new BigDecimal(wMoney.toString())).floatValue();

                    if (audit.getResult().equals(AuditState.PASS)) {
                        count = userWithdrawRepository.updateState(
                                wMoney,
                                realityMoney,
                                "审核服务费：" + FloatHelper.sub(wMoney, realityMoney),
                                new Timestamp(System.currentTimeMillis()),
                                WithdrawState.SUCCESS,
                                userWithdraw.getId()
                        );
                    } else {
                        count = userWithdrawRepository.updateState(WithdrawState.FAILED, userWithdraw.getId());
                        if (count > 0) {
                            //获取用户信息
                            User user = userWithdraw.getUser();
                            Float dMoney = FloatHelper.add(user.getMoney(), userWithdraw.getMoney());
                            count = userRepository.update(dMoney, userWithdraw.getUserId());
                        }
                    }


                    break;

                case PAY:
                    //获取用户提现记录不能为空
                    if (userWithdraw == null) {
                        throw new DBException(StringResourceCenter.DB_QUERY_FAILED);
                    }

                    //根据管理员的设置变动用户真实充值金额
                    Float pMoney = audit.getAuditWithdraw().getAdminMoney();

                    if (audit.getResult().equals(AuditState.PASS)) {
                        count = userWithdrawRepository.updateState(
                                pMoney,
                                pMoney,
                                "充值服务费：" + 0,
                                new Timestamp(System.currentTimeMillis()),
                                WithdrawState.PAY_SUCCESS,
                                userWithdraw.getId()
                        );

                        if (count > 0) {
                            //获取用户信息
                            User user = userWithdraw.getUser();
                            Float dMoney = FloatHelper.add(user.getMoney(), pMoney);
                            count = userRepository.update(dMoney, userWithdraw.getUserId());
                        }

                    } else {
                        count = userWithdrawRepository.updateState(WithdrawState.PAY_FAILED, userWithdraw.getId());
                    }

                case TASK:
                    //获取的任务不能为空
                    if (task == null) {
                        throw new DBException(StringResourceCenter.DB_QUERY_FAILED);
                    }
                    count = taskRepository.updateStateAndAdminAuditTime(
                            task.getId(),
                            audit.getResult().equals(AuditState.PASS) ? TaskState.AUDIT_SUCCESS : TaskState.AUDIT_FAILURE,
                            null,
                            null);
                    break;
                case USER_FAILURE_TASK:
                    //获取的任务不能为空
                    if (task == null) {
                        throw new DBException(StringResourceCenter.DB_QUERY_FAILED);
                    }
                    //当放弃任务需要管理员审核时，不管通过与否都将任务状态置为放弃
                    count = taskRepository.updateStateAndAdminAuditTime(
                            task.getId(),
                            TaskState.ABANDON_OK,
                            null,
                            null);

                    if (count <= 0) {
                        throw new DBException("管理员设置任务状态为放弃时失败");
                    }

                    //不管通过与否， 都需要猎刃数据，用来改变状态或者赔偿猎刃，并且，列表中只会有猎刃拒绝用户的放弃任务的状态
                    List<HunterTask> hunterTasks = hunterTaskRepository.findBy(task.getId(), HunterTaskState.HUNTER_REPULSE);
                    //这里需要获取接了任务的并且拒绝了用户放弃任务的猎刃任务列表，如果不存在，则抛异常
                    if (ListUtils.isEmpty(hunterTasks)) {
                        throw new DBException(StringResourceCenter.DB_RESOURCE_ABNORMAL);
                    }

                    //获取猎刃任务的编号
                    List<String> ids = HunterTask.toIds(hunterTasks);


                    HunterTaskState hunterTaskState = null;
                    if (audit.getResult().equals(AuditState.PASS)) {
                        //用户放弃的任务被通过时，退还用户押金，并修改那些拒绝用户放弃任务的猎刃的任务状态为“被放弃”

                        //获取任务对应用户
                        User user = task.getUser();
                        Float dMoney = FloatHelper.add(user.getMoney(), task.getMoney());
                        //退还用户押金
                        count = userRepository.update(dMoney, task.getUserId());

                        //押金如果没有退还成功
                        if (count <= 0) {
                            throw new DBException(StringResourceCenter.DB_UPDATE_ABNORMAL);
                        }


                        hunterTaskState = HunterTaskState.TASK_BE_ABANDON;



                    } else {
                        //用户放弃的任务审核不通过

                        //取出任务平均补偿金额
                        Float pay = task.getCompensateMoney();

                        //获取接了该任务，并且不同意用户放弃任务的猎刃列表
//                        List<Hunter> queryResult = hunterRepository.findAll((root, query, cb) -> {
//                            List<Predicate> predicates = new ArrayList<>();
//
//                            Subquery<HunterTask> entity = query.subquery(HunterTask.class);
//                            Root<HunterTask> xRoot = entity.from(HunterTask.class);
//                            entity.select(xRoot.get(HunterTask.HUNTER_ID));
//                            Predicate predicate = cb.equal(xRoot.get(HunterTask.TASK_ID), task.getId());
//                            predicate = cb.and(predicate, cb.equal(xRoot.get(HunterTask.HUNTER_TASK_STATE), HunterTaskState.HUNTER_REPULSE));
//                            entity.where(predicate);
//
//                            predicates.add(cb.in(root.get(Hunter.USER_ID)).value(entity));
//
//                            return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
//                        });





                        //用户需要赔偿给猎刃的总金额
                        Float rp = FloatHelper.multiply(pay, (float) hunterTasks.size());
                        //赔偿完后，剩下的钱还给用户
                        Float dm = FloatHelper.sub(task.getMoney(), rp);

                        count = 0;
                        //更新猎刃的账户金额
                        for (HunterTask ht :
                                hunterTasks) {
                            Float dMoney = FloatHelper.add(ht.getHunter().getUser().getMoney(), pay);
                            userRepository.update(dMoney, ht.getHunter().getUserId());
                            count++;
                        }

                        if (count != hunterTasks.size()) {
                            throw new DBException("更新猎刃金额失败");
                        }

                        //添加用户转账记录
                        List<UserIeRecord> userIeRecords = new ArrayList<>();
                        hunterTasks.forEach(hunter -> {
                            UserIeRecord userIeRecord = new UserIeRecord();
                            userIeRecord.setMe(task.getUserId());
                            userIeRecord.setTo(hunter.getHunterId());
                            userIeRecord.setContext("来自 " + task.getName() + " 的补偿");
                            userIeRecord.setId(IdGenerator.INSTANCE.nextId());
                            userIeRecord.setMoney(pay);
                            userIeRecords.add(userIeRecord);
                        });

                        userIeRecordRepository.save(userIeRecords);

                        if (dm > 0){
                            //获取任务对应用户
                            User user = task.getUser();
                            Float dMoney = FloatHelper.add(user.getMoney(), dm);
                            //退还用户剩下的押金
                            count = userRepository.update(dMoney, task.getUserId());
                            if (count <= 0){
                                throw new DBException("更新用户金额失败");
                            }
                        }

                        hunterTaskState = HunterTaskState.END_OK;
                    }

                    count = hunterTaskRepository.updateStateAndAdminAuditTime(ids, hunterTaskState);

                    //必须将列表中的猎刃任务状态都修改成功
                    if (count != ids.size()) {
                        throw new DBException(StringResourceCenter.DB_UPDATE_ABNORMAL);
                    }

                    break;
                case HUNTER_FAILURE_TASK:
                    //获取的猎刃任务不能为空
                    if (hunterTask == null) {
                        throw new DBException(StringResourceCenter.DB_QUERY_FAILED);
                    }
                    //当猎刃放弃任务需要管理员审核时，不管通过与否都将任务状态置为放弃
                    count = hunterTaskRepository.updateStateAndAdminAuditTime(hunterTask.getId(),
                            HunterTaskState.TASK_ABANDON,null,null);

                    if (count <= 0) {
                        throw new DBException(StringResourceCenter.DB_UPDATE_ABNORMAL);
                    }

                    //猎刃执行的任务
                    Task noTask = hunterTask.getTask();

                    //只有未被接满的任务，状态才会等于ISSUE
                    if (noTask.getState() != TaskState.ISSUE) {
                        //不管通过与否，都要修改用户发布任务的任务状态
                        count = taskRepository.updateStateAndIssueTime(noTask.getId(), TaskState.ISSUE, new Timestamp(System.currentTimeMillis()));

                        if (count <= 0) {
                            throw new DBException(StringResourceCenter.DB_UPDATE_ABNORMAL);
                        }
                    }

                    if (audit.getResult().equals(AuditState.PASS)) {
                        //审核通过

                        //获取对应的猎刃信息
                        Hunter hunter = hunterTask.getHunter();
                        Float dMoney = FloatHelper.add(hunter.getUser().getMoney(), noTask.getCompensateMoney());
                        //将猎刃的押金退回
                        count = userRepository.update(dMoney, hunterTask.getHunterId());


                    } else {
                        //审核不通过

                        //获取对应的用户信息
                        User user = noTask.getUser();
                        Float dMoney = FloatHelper.add(user.getMoney(), noTask.getCompensateMoney());
                        //将猎刃押金作为用户补偿，用户需要新增一条转账记录
                        count = userRepository.update(dMoney, noTask.getUserId());

                        if (count > 0) {
                            UserIeRecord userIeRecord = new UserIeRecord();
                            userIeRecord.setMe(hunterTask.getHunterId());
                            userIeRecord.setTo(noTask.getUserId());
                            userIeRecord.setContext("来自 " + noTask.getName() + " 的补偿");
                            userIeRecord.setId(IdGenerator.INSTANCE.nextId());
                            userIeRecord.setMoney(noTask.getCompensateMoney());

                            userIeRecordRepository.save(userIeRecord);
                        }

                    }
                    break;
                case HUNTER_OK_TASK:
                    //获取的猎刃任务不能为空
                    if (hunterTask == null) {
                        throw new DBException(StringResourceCenter.DB_QUERY_FAILED);
                    }
                    //获取对应任务
                    Task hTask = hunterTask.getTask();
                    //获取任务对应的用户信息
                    User hTaskUser = hTask.getUser();
                    //获取对应的猎刃信息
                    Hunter hunter = hunterTask.getHunter();

                    switch (result.getResult()) {
                        case REWORK:
                            //管理员让猎刃重做任务
                            count = hunterTaskRepository.updateStateAndAdminAuditTime(
                                    hunterTask.getId(),
                                    HunterTaskState.EXECUTE,
                            null,null);
                            if (count <= 0) {
                                throw new DBException("设置任务状态失败");
                            }
                            break;
                        case ABANDON_COMPENSATE:
                            //管理员让猎刃放弃任务，并且补偿用户
                            count = hunterTaskRepository.updateStateAndAdminAuditTime(hunterTask.getId(),
                                    HunterTaskState.TASK_ABANDON,null,null);
                            if (count <= 0) {
                                throw new DBException("设置任务状态失败");
                            }

                            //将猎刃押金作为补偿给用户
                            Float dMoney = FloatHelper.add(hTaskUser.getMoney(), hTask.getCompensateMoney());
                            count = userRepository.update(dMoney, hTaskUser.getId());

                            //新增转账记录
                            if (count > 0) {
                                String context = "来自 " + hTask.getName() + " 的补偿";
                                UserIeRecord userIeRecord = UserIeRecord.init(hunterTask.getHunterId(), hTask.getUserId(), context, hTask.getCompensateMoney());
                                userIeRecordRepository.save(userIeRecord);
                            }
                            break;
                        case ABANDON_NOT_COMPENSATE:
                            //管理员让猎刃放弃任务
                            count = hunterTaskRepository.updateStateAndAdminAuditTime(hunterTask.getId(),
                                    HunterTaskState.TASK_ABANDON,null,null);
                            if (count <= 0) {
                                throw new DBException("设置任务状态失败");
                            }
                            break;
                        case TASK_OK:
                            //管理员让猎刃任务直接完成
                            count = hunterTaskRepository.updateStateAndAdminAuditTime(hunterTask.getId(), HunterTaskState.END_OK,
                                    null,null);
                            if (count <= 0) {
                                throw new DBException("设置任务状态失败");
                            }

                            //为猎刃发赏金与退回押金
                            Float yMoney = hTask.getCompensateMoney();
                            Float sMoney = FloatHelper.divied(hTask.getOriginalMoney(), hTask.getPeopleNumber().floatValue());
                            Float nMoney = FloatHelper.addToBD(yMoney, sMoney).add(FloatHelper.toBig(hunter.getUser().getMoney())).floatValue();
                            count = userRepository.update(nMoney, hunterTask.getHunterId());
                            if (count <= 0) {
                                throw new DBException("设置金额失败");
                            }
                            //添加猎刃的转账记录
                            UserIeRecord record = userIeRecordRepository.save(
                                    UserIeRecord.init(
                                            hTask.getUserId(),
                                            hunterTask.getHunterId(),
                                            "来自任务（" + hTask.getName() + ")的赏金",
                                            sMoney));
                            if (record == null) {
                                throw new DBException("添加转账记录失败");
                            }
                            //修改任务剩余赏金
                            Float lMoney = FloatHelper.sub(hTask.getMoney(), sMoney);

                            //动态金额里面没有赏金了，则说明用户任务已经完成被完成了
                            if (lMoney <= 0){
                                count = taskRepository.updateMoneyAndState(lMoney, TaskState.FINISH,hTask.getId());
                            }else {
                                count = taskRepository.updateMoney(lMoney, hTask.getId());
                            }
                            if (count <= 0) {
                                throw new DBException("设置任务金额失败");
                            }



                            break;
                        default:
                            break;
                    }

                    if (!result.getResult().equals(AuditState.REWORK) && !result.getResult().equals(AuditState.TASK_OK)) {
                        //判断用户是否需要重新发布任务
                        if (TaskState.isReIssue(hTask.getState())) {
                            count = taskRepository.updateStateAndIssueTime(hTask.getId(), TaskState.ISSUE, TimestampHelper.today());
                        }
                    }


                    break;
                default:
                    break;
            }
            if (count <= 0) {
                throw new DBException(StringResourceCenter.DB_UPDATE_ABNORMAL);
            }
        }
        return result;
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public Audit findOne(String id) {
        return auditRepository.findOne(id);
    }
}
