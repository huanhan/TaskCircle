package com.tc.controller;

import com.tc.db.entity.*;
import com.tc.db.enums.*;
import com.tc.dto.Result;
import com.tc.dto.TimeScope;
import com.tc.dto.audit.AuditResult;
import com.tc.dto.audit.QueryAudit;
import com.tc.dto.comment.QueryUserComment;
import com.tc.dto.enums.DateType;
import com.tc.dto.enums.TaskConditionResult;
import com.tc.dto.enums.TaskConditionSelect;
import com.tc.dto.finance.QueryFinance;
import com.tc.dto.finance.QueryIE;
import com.tc.dto.message.QueryMessage;
import com.tc.dto.statistics.TaskCondition;
import com.tc.dto.task.QueryTask;
import com.tc.dto.trans.*;
import com.tc.dto.user.*;
import com.tc.exception.DBException;
import com.tc.exception.ValidException;
import com.tc.service.*;
import com.tc.until.ListUtils;
import com.tc.until.StringResourceCenter;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 普通用户管理控制器
 *
 * @author Cyg
 */
@RestController
@ResponseStatus(code = HttpStatus.OK)
@RequestMapping("/user")
public class UserController {

    public static final int NOT_LOGIN_TIME = 180;

    @Autowired
    private UserService userService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskClassifyService taskClassifyService;

    @Autowired
    private CommentUserService commentUserService;

    @Autowired
    private AuditService auditService;

    @Autowired
    private UserWithdrawService userWithdrawService;

    @Autowired
    private UserIeRecordService userIeRecordService;

    @Autowired
    private UserImgService userImgService;

    @Autowired
    private UserContactService userContactService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private AuditWithdrawService auditWithdrawService;

    /**
     * 根据查询条件获取用户列表
     *
     * @param queryUser 用户查询条件
     * @return
     */
    @PostMapping
    @ApiOperation(value = "根据查询条件获取用户列表")
    public Result all(@RequestBody QueryUser queryUser) {

        queryUser.setCategory(UserCategory.NORMAL);
        Page<User> queryUsers = userService.findByQueryUser(queryUser);
        TransStates result = new TransStates(UserState.toList(), User.toIndexAsList(queryUsers.getContent()));
        return Result.init(result, queryUser.append(queryUsers.getTotalElements(), (long) queryUsers.getTotalPages()));

    }

    /**
     * 获取用户信息详情
     *
     * @param id
     * @return
     */
    @GetMapping("/{id:\\d+}")
    @ApiOperation(value = "获取用户详情信息")
    public User detail(@PathVariable("id") Long id) {

        User user = userService.findOne(id);

        if (!user.getCategory().equals(UserCategory.NORMAL)) {
            throw new ValidException(StringResourceCenter.VALIDATOR_AUTHORITY_FAILED);
        }

        return User.toDetail(user);

    }


    /**
     * 获取用户数量
     *
     * @param queryUser
     * @return
     */
    @PostMapping("/count/query")
    @ApiOperation(value = "获取查询条件的用户数量")
    public Result userCountByQuery(@RequestBody QueryUser queryUser) {
        queryUser.setCategory(UserCategory.NORMAL);
        long count = userService.countByQuery(queryUser);
        return Result.init(count, queryUser);
    }


    @GetMapping("/task/querySetting/{id:\\d+}")
    @ApiOperation(value = "获取指定用户任务统计信息可设置的内容")
    public TransTaskConditionQuery getTaskQueryByUser(@PathVariable("id") Long id) {
        User user = userService.findOne(id);
        if (user == null) {
            throw new DBException(StringResourceCenter.DB_QUERY_ABNORMAL);
        }
        List<TransEnum> cResults = TaskConditionResult.toTrans();
        List<TransEnum> cSelects = TaskConditionSelect.toTrans();
        List<TransEnum> transTypes = TaskType.toList();
        List<TransEnum> transStates = TaskState.toList();
        List<TaskClassify> classifies = taskClassifyService.findUserTaskAllClassify(id);
        TransTaskConditionQuery result = new TransTaskConditionQuery();
        result.setcResult(cResults);
        result.setcSelect(cSelects);
        result.setTaskTypes(transTypes);
        result.setTaskStates(transStates);
        result.setTaskClassifies(TaskClassify.toTrans(classifies));
        return result;
    }

    /**
     * 根据用户编号获取用户的任务统计信息
     *
     * @param id 用户编号
     * @return
     */
    @PostMapping("/statistics/{id:\\d+}/out/task")
    @ApiOperation(value = "根据用户编号获取用户任务统计信息")
    public Result getTaskStatisticsByUser(@PathVariable("id") Long id, @Valid @RequestBody TaskCondition condition, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidException(bindingResult.getFieldErrors());
        }
        Timestamp[] dc = DateCondition.reset(condition.getBegin(), condition.getEnd());
        List<Task> query = taskService.findByQueryTaskAndNotPage(QueryTask.init(id, dc[0], dc[1]));
        if (ListUtils.isEmpty(query)) {
            throw new DBException("任务：没有需要统计的数据");
        }
        User user = query.get(0).getUser();
        List<TaskClassify> classifies = null;
        if (condition.getSelect().equals(TaskConditionSelect.CLASSIFY)) {

            if (ListUtils.isNotEmpty(condition.getItems())) {
                classifies = taskClassifyService.findByIds(TaskCondition.toIds(condition.getItems()));
            } else {
                classifies = taskClassifyService.findUserTaskAllClassify(id);
            }

            if (ListUtils.isEmpty(classifies)) {
                throw new DBException("分类：" + StringResourceCenter.DB_QUERY_FAILED);
            }
        }
        UserTaskStatistics result = UserTaskStatistics.statistics(query, condition, classifies);
        return Result.init(UserTaskStatistics.filter(result, condition.getResult()).appand(user), condition);
    }


    /**
     * 根据用户编号和统计条件获取用户收支统计情况
     *
     * @param id        用户编号
     * @param condition 统计条件
     * @return
     */
    @PostMapping("/statistics/{id:\\d+}/ws")
    @ApiOperation(value = "根据用户编号和统计条件获取用户收支统计情况")
    public Result getUserWithdrawStatistics(@PathVariable("id") Long id,
                                            @RequestBody DateCondition condition) {
        if (condition.getType() == null) {
            condition.setType(DateType.HOURS);
        }
        condition = DateCondition.reset(condition);
        List<UserWithdraw> query = userWithdrawService.findByQueryFinanceNotPage(QueryFinance.init(id, condition.getBegin(), condition.getEnd()));
        if (ListUtils.isEmpty(query)) {
            throw new ValidException("收支：" + StringResourceCenter.DB_QUERY_FAILED);
        }
        UserWithdrawStatistics userWithdrawStatistics = UserWithdrawStatistics.statistics(query, condition.getType());
        User user = query.get(0).getUser();
        userWithdrawStatistics.setUser(new User(user.getId(), user.getName(), user.getUsername()));
        return Result.init(userWithdrawStatistics, condition);
    }

    /**
     * 根据用户编号和收支查询条件获取收支列表
     *
     * @param id           用户编号
     * @param queryFinance 查询条件
     * @return
     */
    @PostMapping("/ws/{id:\\d+}")
    @ApiOperation(value = "根据用户编号和收支查询条件获取收支列表")
    public Result getUserWithdraw(@PathVariable("id") Long id, @RequestBody QueryFinance queryFinance) {
        User user = userService.findOne(id);
        if (user == null) {
            throw new ValidException("用户不存在");
        }
        queryFinance.setStates(WithdrawState.toUserList());
        queryFinance.setUserId(id);
        queryFinance.setSort(new Sort(Sort.Direction.DESC, UserWithdraw.CREATE_TIME));
        Page<UserWithdraw> query = userWithdrawService.findByQueryFinance(queryFinance);
        TransStatesTwo result = new TransStatesTwo(
                WithdrawState.toUserTransList(),
                WithdrawType.toList(),
                UserWithdraw.toIndexAsList(query.getContent()),
                new Trans(id, user.toTitle()));
        return Result.init(result, queryFinance.append(query.getTotalElements(), (long) query.getTotalPages()));
    }

    /**
     * 获取收支详情
     *
     * @return
     */
    @GetMapping("/ws/detail/{id}/{aid:\\d+}")
    @ApiOperation(value = "获取指定用户的指定收支详情信息")
    public UserWithdraw ieSource(@PathVariable("id") String id, @PathVariable("aid") Long aid) {
        UserWithdraw result = userWithdrawService.findOne(id);
        if (!result.getUserId().equals(aid) || WithdrawState.inNotDetail(result.getState())) {
            throw new ValidException(StringResourceCenter.VALIDATOR_AUTHORITY_FAILED);
        }
        List<AuditWithdraw> awList = auditWithdrawService.findByWithdrawId(result.getId());
        return UserWithdraw
                .toDetail(result)
                .append(WithdrawState.toUserTransList(), WithdrawType.toList())
                .append(AuditWithdraw.toIndexList(awList));
    }

    /**
     * 根据查询条件获取任务列表
     *
     * @param queryTask 查询条件
     * @return
     */
    @PostMapping("/task/{id:\\d+}")
    @ApiOperation(value = "获取任务列表")
    public Result all(@RequestBody QueryTask queryTask, @PathVariable("id") Long id) {
        User user = userService.findOne(id);
        if (user == null) {
            throw new ValidException("用户不存在");
        }
        queryTask.setTaskId(id);
        Page<Task> queryTasks = taskService.findByQueryTask(queryTask);
        List<Task> result = Task.toIndexAsList(queryTasks.getContent());
        TransData trans = new TransData(user.getId(), user.toTitle(), result);
        return Result.init(trans, queryTask.append(queryTasks.getTotalElements(), (long) queryTasks.getTotalPages()));
    }

    /**
     * 获取用户被评论信息
     *
     * @param queryUserComment 用户评论查询条件
     * @return
     */
    @PostMapping("/comment/{id:\\d+}")
    @ApiOperation(value = "根据用户编号获取用户的被评论列表")
    public Result getUserComment(@RequestBody QueryUserComment queryUserComment, @PathVariable("id") Long id) {
        queryUserComment.setUserId(id);
        queryUserComment.setSort(new Sort(Sort.Direction.DESC, CommentUser.COMMENT_ID));
        Page<CommentUser> query = commentUserService.findByQuery(queryUserComment);
        return Result.init(CommentUser.toListInIndex(query.getContent()), queryUserComment.append(query.getTotalElements(), (long) query.getTotalPages()).clearSort());
    }

    /**
     * 根据用户编号获取用户的审核历史记录
     * 包括提现审核，发布任务的审核，放弃任务的审核
     *
     * @param id
     * @param queryAudit
     * @return
     */
    @PostMapping("/audit/{id:\\d+}")
    @ApiOperation(value = "根据用户编号获取用户的审核历史记录，包括任务审核，猎刃审核，提现审核等")
    public Result getUserAuditRecords(@PathVariable("id") Long id, @RequestBody QueryAudit queryAudit) {
        User user = userService.findOne(id);
        if (user == null) {
            throw new ValidException("用户不存在");
        }

        if (queryAudit.getType() != null) {
            if (user.getCategory().equals(UserCategory.NORMAL)) {
                if (!AuditType.isAuditUser(queryAudit.getType())) {
                    throw new ValidException("状态类别错误");
                }
            }
        }

        queryAudit.setUserId(id);
        queryAudit.setCategory(user.getCategory());
        Page<Audit> audits = auditService.findByQueryAndUser(queryAudit);
        AuditResult result = new AuditResult(
                AuditState.toList(),
                AuditType.toList(),
                Audit.toListInIndex(audits.getContent()),
                new Trans(user.getId(), user.toTitle()));
        return Result.init(result, queryAudit.append(audits.getTotalElements(), (long) audits.getTotalPages()));
    }

    /**
     * 根据用户编号、审核类别、审核编号获取审核详情
     *
     * @param id  审核编号
     * @param aid 用户编号
     * @return
     */
    @GetMapping("/audit/{id}/{aid:\\d+}")
    @ApiOperation(value = "根据用户编号、审核类别、审核编号获取审核详情")
    public Result getUserAuditRecord(@PathVariable("id") String id,
                                     @PathVariable("aid") Long aid) {
        Audit query = auditService.findOne(id);
        if (!AuditType.isAuditUser(query.getType())) {
            throw new ValidException("类别异常");
        }
        Object result = null;
        Long userId = 0L;
        AuditTask auditTask;
        switch (query.getType()) {
            case WITHDRAW:
                AuditWithdraw auditWithdraw = AuditWithdraw.getBy(query);
                userId = auditWithdraw.getUserWithdraw().getUserId();
                result = auditWithdraw;
                break;
            case TASK:
                auditTask = AuditTask.getBy(query);
                userId = auditTask.getTask().getUserId();
                result = auditTask;
                break;
            case USER_FAILURE_TASK:
                auditTask = AuditTask.getBy(query);
                userId = auditTask.getTask().getUserId();
                result = auditTask;
                break;
            default:
                break;
        }
        if (result == null) {
            throw new ValidException("获取数据失败");
        }
        if (!userId.equals(aid)) {
            throw new ValidException(StringResourceCenter.VALIDATOR_AUTHORITY_FAILED);
        }
        return Result.init(result, new QueryAudit(query.getType()));
    }

    /**
     * 获取用户转账记录列表
     *
     * @param id
     * @param queryIE
     * @return
     */
    @PostMapping("/ie/query/{id:\\d+}")
    @ApiOperation(value = "获取用户转账记录列表")
    public Result userIERecord(@PathVariable("id") Long id, @RequestBody QueryIE queryIE) {
        User user = userService.findOne(id);
        if (user == null) {
            throw new ValidException("用户不存在");
        }
        if (queryIE.getIeType() != null) {
            switch (queryIE.getIeType()) {
                case IN:
                    queryIE.setTo(id);
                    break;
                case OUT:
                    queryIE.setMe(id);
                    break;
                default:
                    break;
            }
        } else {
            queryIE.setMe(id);
            queryIE.setTo(id);
        }

        Page<UserIeRecord> query = userIeRecordService.findByQuery(queryIE);
        TransData result = new TransData(user.getId(), user.toTitle(), UserIeRecord.toListInIndex(query.getContent()));
        return Result.init(result, queryIE.append(query.getTotalElements(), (long) query.getTotalPages()));
    }

    /**
     * 获取用户转账记录详情
     *
     * @param id
     * @param aid
     * @return
     */
    @GetMapping("/ie/detail/{id:\\d+}/{aid:\\d+}")
    @ApiOperation(value = "获取用户转账记录详情")
    public UserIeRecord userIeDetail(@PathVariable("id") String id, @PathVariable("aid") Long aid) {
        UserIeRecord ieRecord = userIeRecordService.findOne(id);
        if (ieRecord == null) {
            throw new DBException(StringResourceCenter.DB_QUERY_FAILED);
        }
        if (!ieRecord.getTo().equals(aid) && !ieRecord.getMe().equals(aid)) {
            throw new ValidException("用户信息不正确");
        }
        return UserIeRecord.toDetail(ieRecord).append(aid);
    }

    /**
     * 获取用户的图片资料
     *
     * @param id
     * @return
     */
    @GetMapping("/img/{id:\\d+}")
    @ApiOperation(value = "获取用户的图片资料")
    public Result userImg(@PathVariable("id") Long id) {
        User user = userService.findOne(id);
        if (user == null) {
            throw new DBException(StringResourceCenter.DB_QUERY_FAILED);
        }
        List<UserImg> query = userImgService.findByUser(id);
        TransData trans = new TransData(user.getId(), user.toTitle(), UserImg.toListInIndex(query));
        return Result.init(trans);
    }

    /**
     * 获取用户所有的联系方式
     *
     * @param id
     * @return
     */
    @GetMapping("/contact/{id:\\d+}")
    @ApiOperation(value = "获取用户所有的联系方式")
    public Result userContact(@PathVariable("id") Long id) {
        User user = userService.findOne(id);
        if (user == null) {
            throw new DBException(StringResourceCenter.DB_QUERY_FAILED);
        }
        List<UserContact> query = userContactService.findByUser(id);
        TransData trans = new TransData(user.getId(), user.toTitle(), UserContact.toListInIndex(query));
        return Result.init(trans);
    }

    /**
     * 获取用户所有可查看的系统消息
     *
     * @param id
     * @return
     */
    @GetMapping("/message/{id:\\d+}")
    @ApiOperation(value = "获取用户所有可查看的系统消息")
    public Result userMessage(@PathVariable("id") Long id) {
        User user = userService.findOne(id);
        if (user == null) {
            throw new DBException(StringResourceCenter.DB_QUERY_FAILED);
        }
        List<Message> queryMessage = messageService.findByQueryAndNotPage(QueryMessage.init(MessageState.NORMAL));
        List<Message> result = Message.byUser(queryMessage, user);
        return Result.init(Message.toListByIndex(result));
    }

    /**
     * 获取用户押金列表
     *
     * @param id
     * @param scope
     * @return
     */
    @PostMapping("/cp/{id:\\d+}")
    @ApiOperation(value = "获取用户的押金列表")
    public Result userCashPledge(@PathVariable("id") Long id, @RequestBody TimeScope scope) {
        scope.setId(id);
        List<CashPledge> result = userService.findByCashPledgeAndUser(scope);
        return Result.init(CashPledge.toListInIndex(result), scope);
    }
}
