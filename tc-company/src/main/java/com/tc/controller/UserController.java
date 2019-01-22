package com.tc.controller;

import com.tc.db.entity.*;
import com.tc.db.enums.AuditType;
import com.tc.db.enums.UserCategory;
import com.tc.dto.Result;
import com.tc.dto.audit.QueryAudit;
import com.tc.dto.comment.QueryUserComment;
import com.tc.dto.enums.TaskConditionSelect;
import com.tc.dto.finance.QueryFinance;
import com.tc.dto.finance.QueryIE;
import com.tc.dto.statistics.TaskCondition;
import com.tc.dto.task.QueryTask;
import com.tc.dto.user.*;
import com.tc.exception.DBException;
import com.tc.exception.ValidException;
import com.tc.service.*;
import com.tc.until.ListUtils;
import com.tc.until.StringResourceCenter;
import com.tc.until.TimestampHelper;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.method.P;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * 普通用户管理控制器
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

    /**
     * 根据查询条件获取用户列表
     * @param queryUser 用户查询条件
     * @return
     */
    @PostMapping
    @ApiOperation(value = "根据查询条件获取用户列表")
    public Result all(@RequestBody QueryUser queryUser){

        queryUser.setCategory(UserCategory.NORMAL);
        Page<User> queryUsers = userService.findByQueryUser(queryUser);
        List<User> result = User.toIndexAsList(queryUsers.getContent());
        return Result.init(result,queryUser);

    }

    /**
     * 获取用户信息详情
     * @param id
     * @return
     */
    @GetMapping("/{id:\\+d}")
    @ApiOperation(value = "获取用户详情信息")
    public User detail(@PathVariable("id") Long id){

        User user = userService.findOne(id);
        return User.toDetail(user);

    }



    /**
     * 获取用户数量
     * @param queryUser
     * @return
     */
    @PostMapping("/count/query")
    @ApiOperation(value = "获取查询条件的用户数量")
    public Result userCountByQuery(@RequestBody QueryUser queryUser){
        long count = userService.countByQuery(queryUser);
        return Result.init(count,queryUser);
    }

    /**
     * 根据用户编号获取用户的任务统计信息
     * @param id 用户编号
     * @return
     */
    @PostMapping("/statistics/{id:\\d+}/out/task")
    @ApiOperation(value = "根据用户编号获取用户任务统计信息")
    public UserTaskStatistics getTaskStatisticsByUser(@PathVariable("id") Long id,@Valid @RequestBody TaskCondition condition,BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            throw new ValidException(bindingResult.getFieldErrors());
        }
        List<Task> query = taskService.findByQueryTaskAndNotPage(QueryTask.init(id,condition.getBegin(),condition.getEnd()));
        if (ListUtils.isEmpty(query)){
            throw new DBException("任务：" + StringResourceCenter.DB_QUERY_FAILED);
        }
        List<TaskClassify> classifies = null;
        if (condition.getSelect().equals(TaskConditionSelect.CLASSIFY)){
            classifies = taskClassifyService.findByIds(TaskCondition.toIds(condition.getItems()));
            if (ListUtils.isEmpty(classifies)){
                throw new DBException("分类：" + StringResourceCenter.DB_QUERY_FAILED);
            }
        }
        UserTaskStatistics result = UserTaskStatistics.statistics(query,condition,classifies);
        return UserTaskStatistics.filter(result,condition.getResult());
    }




    /**
     * 根据用户编号和统计条件获取用户收支统计情况
     * @param id 用户编号
     * @param condition 统计条件
     * @return
     */
    @PostMapping("/statistics/{id:\\d+}/ws")
    @ApiOperation(value = "根据用户编号和统计条件获取用户收支统计情况")
    public UserWithdrawStatistics getUserWithdrawStatistics(@PathVariable("id") Long id,
                                                            @RequestBody DateCondition condition){
        List<UserWithdraw> query = userWithdrawService.findByQueryFinanceNotPage(QueryFinance.init(id,condition.getBegin(),condition.getEnd()));
        UserWithdrawStatistics userWithdrawStatistics = UserWithdrawStatistics.statistics(query,condition.getType());
        userWithdrawStatistics.setUser(User.toDetail(query.get(0).getUser()));
        return userWithdrawStatistics;
    }

    /**
     * 根据用户编号和收支查询条件获取收支列表
     * @param id 用户编号
     * @param queryFinance 查询条件
     * @return
     */
    @GetMapping("/ws/{id:\\d+}")
    public Result getUserWithdraw(@PathVariable("id") Long id,@RequestBody QueryFinance queryFinance){
        queryFinance.setUserId(id);
        queryFinance.setSort(new Sort(Sort.Direction.DESC,UserWithdraw.CREATE_TIME));
        Page<UserWithdraw> query = userWithdrawService.findByQueryFinance(queryFinance);
        return Result.init(UserWithdraw.toIndexAsList(query.getContent()),queryFinance);
    }

    /**
     * 根据查询条件获取任务列表
     * @param queryTask 查询条件
     * @return
     */
    @PostMapping("/task/{id:\\d+}")
    @ApiOperation(value = "获取任务列表")
    public Result all(@RequestBody QueryTask queryTask, @PathVariable("id") Long id){
        queryTask.setTaskId(id);
        Page<Task> queryTasks = taskService.findByQueryTask(queryTask);
        List<Task> result = Task.toIndexAsList(queryTasks.getContent());
        return Result.init(result,queryTask);
    }

    /**
     * 获取用户评论信息
     * @param queryUserComment 用户评论查询条件
     * @return
     */
    @PostMapping("/comment/{id:\\d+}")
    @ApiOperation(value = "根据用户编号获取用户的评论列表")
    public Result getUserComment(@RequestBody QueryUserComment queryUserComment, @PathVariable("id") Long id){
        queryUserComment.setUserId(id);
        Page<CommentUser> query = commentUserService.findByQuery(queryUserComment);
        return Result.init(CommentUser.toListInIndex(query.getContent()),queryUserComment);
    }

    /**
     * 根据用户编号获取用户的审核历史记录
     * 包括提现审核，发布任务的审核，放弃任务的审核
     * @param id
     * @param queryAudit
     * @return
     */
    @GetMapping("/audit/{id:\\d+}")
    @ApiOperation(value = "根据用户编号获取用户的审核历史记录，包括任务审核，猎刃审核，提现审核等")
    public Result getUserAuditRecords(@PathVariable("id") Long id,@RequestBody QueryAudit queryAudit){
        if (!AuditType.isAuditUser(queryAudit.getType())){
            throw new ValidException("状态类别错误");
        }
        queryAudit.setUserId(id);
        Page<Audit> audits = auditService.findByQueryAndUser(queryAudit);
        return Result.init(Audit.toListInIndex(audits.getContent()),queryAudit);
    }

    /**
     * 根据用户编号、审核类别、审核编号获取审核详情
     * @param id 用户编号
     * @param aid 审核编号
     * @return
     */
    @GetMapping("/audit/{id:\\d+}/{aid:\\d+}")
    @ApiOperation(value = "根据用户编号、审核类别、审核编号获取审核详情")
    public Result getUserAuditRecord(@PathVariable("id") Long id,
                                     @PathVariable("aid") String aid){
        Audit query = auditService.findOne(aid);
        if (!AuditType.isAuditUser(query.getType())){
            throw new ValidException("类别异常");
        }
        Object result = null;
        Long userId = 0L;
        AuditTask auditTask;
        switch (query.getType()){
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
        if (result == null){
            throw new ValidException("获取数据失败");
        }
        if (!userId.equals(id)){
            throw new ValidException(StringResourceCenter.VALIDATOR_AUTHORITY_FAILED);
        }
        return Result.init(result,new QueryAudit(query.getType()));
    }

    /**
     * 获取用户转账记录列表
     * @param id
     * @param queryIE
     * @return
     */
    @PostMapping("/ie/query/{id:\\d+}")
    @ApiOperation(value = "获取用户转账记录列表")
    public Result userIERecord(@PathVariable("id") Long id, @RequestBody QueryIE queryIE){
        queryIE.setMe(id);
        queryIE.setTo(id);
        Page<UserIeRecord> query = userIeRecordService.findByQuery(queryIE);
        return Result.init(UserIeRecord.toListInIndex(query.getContent()),queryIE);
    }

    /**
     * 获取用户转账记录详情
     * @param id
     * @param ieId
     * @return
     */
    @GetMapping("/ie/detail/{ieId:\\d+}/{id:\\d+}")
    @ApiOperation(value = "获取用户转账记录详情")
    public UserIeRecord userIeDetail(@PathVariable("id") Long id, @PathVariable("ieId") String ieId){
        UserIeRecord ieRecord = userIeRecordService.findOne(ieId);
        if (ieRecord == null){
            throw new DBException(StringResourceCenter.DB_QUERY_FAILED);
        }
        if (!ieRecord.getTo().equals(id) && !ieRecord.getMe().equals(id)){
            throw new ValidException("用户信息不正确");
        }
        return UserIeRecord.toDetail(ieRecord);
    }

    /**
     * 获取用户的图片资料
     * @param id
     * @return
     */
    @GetMapping("/img/{id:\\d+}")
    @ApiOperation(value = "获取用户的图片资料")
    public Result userImg(@PathVariable("id") Long id){
        List<UserImg> query = userImgService.findByUser(id);
        return Result.init(UserImg.toListInIndex(query));
    }

    /**
     * 获取用户所有的联系方式
     * @param id
     * @return
     */
    @GetMapping("/contact/{id:\\d+}")
    @ApiOperation(value = "获取用户所有的联系方式")
    public Result userContact(@PathVariable("id") Long id){
        List<UserContact> query = userContactService.findByUser(id);
        return Result.init(UserContact.toListInIndex(query));
    }
}
