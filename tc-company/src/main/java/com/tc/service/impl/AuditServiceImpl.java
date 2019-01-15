package com.tc.service.impl;

import com.tc.db.entity.*;
import com.tc.db.enums.*;
import com.tc.db.repository.*;
import com.tc.dto.audit.QueryAudit;
import com.tc.exception.DBException;
import com.tc.service.AuditHunterService;
import com.tc.service.AuditService;
import com.tc.until.FloatHelper;
import com.tc.until.IdGenerator;
import com.tc.until.ListUtils;
import com.tc.until.StringResourceCenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * 审核服务的实现
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

    @Transactional(rollbackFor = RuntimeException.class,readOnly = true)
    @Override
    public Page<Audit> findByQueryAudit(QueryAudit queryAudit) {
        return auditRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = QueryAudit.initPredicates(queryAudit,root,query,cb);

            return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        },queryAudit);
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public Audit save(Audit audit) {
        Audit result = auditRepository.save(audit);
        if (result != null){
            int count = 0;
            final Task task = (result.getAuditTask() == null ? null : taskRepository.findOne(result.getAuditTask().getTaskId()));
            final UserWithdraw userWithdraw = (result.getAuditWithdraw() == null ? null : userWithdrawRepository.findOne(result.getAuditWithdraw().getWithdrawId()));
            final HunterTask hunterTask = (result.getAuditHunterTask() == null ? null : hunterTaskRepository.findOne(result.getAuditHunterTask().getHunterTaskId()));
            switch (audit.getType()){
                case HUNTER:
                    UserCategory userCategory = null;
                    //如果审核通过，设置用户为猎刃
                    if (audit.getResult().equals(AuditState.PASS)){
                        userCategory = UserCategory.HUNTER;
                    }
                    //不管是否通过，都会将人物状态修改为正常
                    count = userRepository.update(UserState.NORMAL,userCategory,audit.getAuditHunter().getUserId());
                    break;
                case WITHDRAW:

                    //获取用户提现记录不能为空
                    if (userWithdraw == null){
                        throw new DBException(StringResourceCenter.DB_QUERY_FAILED);
                    }

                    WithdrawState withdrawState = WithdrawState.FAILED;
                    //如果审核通过，设置提现状态为成功
                    if (audit.getResult().equals(AuditState.PASS)) {
                        withdrawState = WithdrawState.SUCCESS;
                    }
                    count = userWithdrawRepository.updateState(withdrawState,audit.getAuditWithdraw().getWithdrawId());

                    if (count > 0){
                        //提现失败，需要将提现金额返回到用户账户
                        if (withdrawState.equals(WithdrawState.FAILED)){
                            count = userRepository.update(userWithdraw.getMoney(),userWithdraw.getUserId());
                        }
                    }


                    break;
                case TASK:
                    //获取的任务不能为空
                    if (task == null) {
                        throw new DBException(StringResourceCenter.DB_QUERY_FAILED);
                    }
                    if (audit.getResult().equals(AuditState.PASS)) {
                        //用户新发布的任务审核被通过
                        count = taskRepository.updateState(task.getId(),TaskState.AUDIT_SUCCESS);
                    }else {
                        //用户新发布的任务审核不通过
                        count = taskRepository.updateState(task.getId(),TaskState.AUDIT_FAILUER);
                    }
                    break;
                case USER_FAILE_TASK:
                    //获取的任务不能为空
                    if (task == null) {
                        throw new DBException(StringResourceCenter.DB_QUERY_FAILED);
                    }
                    //当放弃任务需要管理员审核时，不管通过与否都将任务状态置为放弃
                    count = taskRepository.updateState(task.getId(),TaskState.ABANDON_OK);
                    if (audit.getResult().equals(AuditState.PASS)) {
                        //用户放弃的任务被通过时，退还用户押金，并修改那些拒绝用户放弃任务的猎刃的任务状态为“被放弃”
                        if (count > 0){
                            //退还用户押金
                            count = userRepository.update(task.getMoney(),task.getUserId());

                            //押金如果没有退还成功
                            if (count <= 0){
                                throw new DBException(StringResourceCenter.DB_UPDATE_ABNORMAL);
                            }

                            List<HunterTask> hunterTasks = hunterTaskRepository.findBy(task.getId(),HunterTaskState.HUNTER_REPULSE);

                            //这里需要获取接了任务的并且拒绝了用户放弃任务的猎刃任务列表，如果不存在，则抛异常
                            if (ListUtils.isEmpty(hunterTasks)){
                                throw new DBException(StringResourceCenter.DB_RESOURCE_ABNORMAL);
                            }

                            List<String> ids = HunterTask.toIds(hunterTasks);

                            count = hunterTaskRepository.updateState(ids,HunterTaskState.TASK_BE_ABANDON);

                            //必须将列表中的猎刃任务状态都修改成功
                            if (count != ids.size()){
                                throw new DBException(StringResourceCenter.DB_UPDATE_ABNORMAL);
                            }
                        }
                    }else {
                        //用户放弃的任务审核不通过
                        if (count > 0){
                            //取出任务平均补偿金额
                            Float pay = task.getCompensateMoney();

                            //获取接了该任务，并且不同意用户放弃任务的猎刃列表
                            List<Hunter> queryResult = hunterRepository.findAll((root, query, cb) -> {
                                List<Predicate> predicates = new ArrayList<>();

                                Subquery<HunterTask> entity = query.subquery(HunterTask.class);
                                Root<HunterTask> xRoot = entity.from(HunterTask.class);
                                entity.select(xRoot.get(HunterTask.HUNTER_ID));
                                Predicate predicate = cb.equal(xRoot.get(HunterTask.TASK_ID),task.getId());
                                predicate = cb.and(predicate,cb.equal(xRoot.get(HunterTask.HUNTER_TASK_STATE),HunterTaskState.HUNTER_REPULSE));
                                entity.where(predicate);

                                predicates.add(cb.in(root.get(Hunter.USER_ID)).value(entity));

                                return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
                            });

                            List<Long> hunterIds = Hunter.toIds(queryResult);

                            //用户需要赔偿给猎刃的总金额
                            Float rp = FloatHelper.multiply(pay, (float) hunterIds.size());
                            //赔偿完后，剩下的钱还给用户
                            Float dm = FloatHelper.sub(task.getMoney(),rp);

                            //更新猎刃的账户金额
                            count = userRepository.update(hunterIds,pay);

                            //添加用户转账记录
                            List<UserIeRecord> userIeRecords = new ArrayList<>();
                            hunterIds.forEach(aLong -> {
                                UserIeRecord userIeRecord = new UserIeRecord();
                                userIeRecord.setMe(task.getUserId());
                                userIeRecord.setTo(aLong);
                                userIeRecord.setContext("来自 " + task.getName() + " 的补偿");
                                userIeRecord.setId(IdGenerator.INSTANCE.nextId());
                                userIeRecord.setMoney(pay);
                                userIeRecords.add(userIeRecord);
                            });

                            userIeRecordRepository.save(userIeRecords);

                            if (count == hunterIds.size()){
                                //退还用户剩下的押金
                                count = userRepository.update(dm,task.getUserId());
                            }

                        }
                    }
                    break;
                case HUNTER_FAILE_TASK:
                    //获取的猎刃任务不能为空
                    if (hunterTask == null) {
                        throw new DBException(StringResourceCenter.DB_QUERY_FAILED);
                    }
                    //当猎刃放弃任务需要管理员审核时，不管通过与否都将任务状态置为放弃
                    count = hunterTaskRepository.updateState(hunterTask.getId(),HunterTaskState.TASK_ABANDON);

                    if (count <= 0){
                        throw new DBException(StringResourceCenter.DB_UPDATE_ABNORMAL);
                    }

                    //猎刃执行的任务
                    Task noTask = hunterTask.getTask();

                    //只有未被接满的任务，状态才会等于ISSUE
                    if (noTask.getState() != TaskState.ISSUE){
                        //不管通过与否，都要修改用户发布任务的任务状态
                        count = taskRepository.updateStateAndIssueTime(noTask.getId(),TaskState.ISSUE,new Timestamp(System.currentTimeMillis()));

                        if (count <= 0){
                            throw new DBException(StringResourceCenter.DB_UPDATE_ABNORMAL);
                        }
                    }

                    if (audit.getResult().equals(AuditState.PASS)) {
                        //审核通过

                        //将猎刃的押金退回
                        count = userRepository.update(noTask.getCompensateMoney(),hunterTask.getHunterId());


                    }else {
                        //审核不通过

                        //将猎刃押金作为用户补偿，用户需要新增一条转账记录
                        count = userRepository.update(noTask.getCompensateMoney(),noTask.getUserId());

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
                    if (result.getResult().equals(AuditState.NRHC) || result.getResult().equals(AuditState.NRNC)){
                        //设置猎刃任务状态为（结束未完成）
                        count = hunterTaskRepository.updateState(hunterTask.getId(),HunterTaskState.END_NO);

                        if (count <= 0){
                            throw new DBException(StringResourceCenter.DB_UPDATE_ABNORMAL);
                        }

                        //猎刃执行的任务
                        Task okTask = hunterTask.getTask();

                        //设置任务状态
                        //只有未被接满的任务，状态才会等于ISSUE
                        if (okTask.getState() != TaskState.ISSUE){
                            //不管通过与否，都要修改用户发布任务的任务状态
                            count = taskRepository.updateStateAndIssueTime(okTask.getId(),TaskState.ISSUE,new Timestamp(System.currentTimeMillis()));

                            if (count <= 0){
                                throw new DBException(StringResourceCenter.DB_UPDATE_ABNORMAL);
                            }
                        }

                        //补偿流程
                        if (result.getResult().equals(AuditState.NRHC)){
                            //不能重做，需要补偿

                            //将猎刃押金作为补偿给用户
                            count = userRepository.update(okTask.getCompensateMoney(),okTask.getUserId());

                            //新增转账记录
                            if (count > 0) {
                                String context = "来自 " + okTask.getName() + " 的补偿";
                                UserIeRecord userIeRecord = UserIeRecord.init(hunterTask.getHunterId(),okTask.getUserId(),context,okTask.getCompensateMoney());
                                userIeRecordRepository.save(userIeRecord);
                            }
                        }else {
                            //不能重做，不用补偿

                            //退回押金给猎刃
                            count = userRepository.update(okTask.getCompensateMoney(),hunterTask.getHunterId());
                        }
                    }

                    break;
                default:
                    break;
            }
            if (count <= 0){
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
