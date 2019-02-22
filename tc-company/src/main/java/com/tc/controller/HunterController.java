package com.tc.controller;

import com.tc.db.entity.*;
import com.tc.db.enums.HunterTaskState;
import com.tc.db.enums.TaskState;
import com.tc.db.enums.UserCategory;
import com.tc.db.enums.UserState;
import com.tc.dto.Result;
import com.tc.dto.comment.QueryHunterComment;
import com.tc.dto.comment.QueryUserComment;
import com.tc.dto.enums.TaskConditionResult;
import com.tc.dto.enums.TaskConditionSelect;
import com.tc.dto.statistics.TaskCondition;
import com.tc.dto.task.QueryHunterTask;
import com.tc.dto.trans.TransData;
import com.tc.dto.trans.TransEnum;
import com.tc.dto.trans.TransStates;
import com.tc.dto.trans.TransTaskConditionQuery;
import com.tc.dto.user.DateCondition;
import com.tc.dto.user.HunterTaskStatistics;
import com.tc.dto.user.QueryUser;
import com.tc.dto.user.hunter.QueryHunter;
import com.tc.exception.DBException;
import com.tc.exception.ValidException;
import com.tc.service.CommentHunterService;
import com.tc.service.HunterService;
import com.tc.service.HunterTaskService;
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
import java.util.List;

/**
 * 猎刃控制器
 * @author Cyg
 */
@RestController
@ResponseStatus(code = HttpStatus.OK)
@RequestMapping("/hunter")
public class HunterController {

    @Autowired
    private HunterService hunterService;

    @Autowired
    private HunterTaskService hunterTaskService;

    @Autowired
    private CommentHunterService commentHunterService;

    /**
     * 获取猎刃的基本信息
     * @param id 用户编号
     * @return
     */
    @GetMapping("/{id:\\d+}")
    @ApiOperation(value = "根据用户编号获取猎刃的基本信息")
    public Hunter hunterDetail(@PathVariable("id") Long id){
        Hunter hunter = hunterService.findOne(id);
        return Hunter.toDetail(hunter);
    }

    /**
     * 根据查询条件获取猎刃列表
     * @param queryHunter 用户查询条件
     * @return
     */
    @PostMapping
    @ApiOperation(value = "根据查询条件获取用户列表")
    public Result all(@RequestBody QueryHunter queryHunter){

        Page<Hunter> queryHunters = hunterService.findByQuery(queryHunter);
        TransStates result = new TransStates(UserState.toList(),Hunter.toIndexAsList(queryHunters.getContent()));
        return Result.init(result,queryHunter.append(queryHunters.getTotalElements(),(long)queryHunters.getTotalPages()));

    }

    @GetMapping("/task/querySetting")
    @ApiOperation(value = "获取指定猎刃任务统计信息可设置的内容")
    public TransTaskConditionQuery getHunterTaskQuery(){
        List<TransEnum> cResults = TaskConditionResult.toTrans();
        List<TransEnum> cSelects = TaskConditionSelect.toTrans();
        List<TransEnum> transStates = HunterTaskState.toList();
        TransTaskConditionQuery result = new TransTaskConditionQuery();
        result.setcResult(cResults);
        result.setcSelect(cSelects);
        result.setTaskStates(transStates);
        return result;
    }


    /**
     * 根据用户编号获取猎刃接受的任务的统计信息
     * @param id 用户编号
     * @return
     */
    @PostMapping("/statistics/{id:\\d+}/in/task")
    @ApiOperation(value = "根据用户编号获取猎刃接受的任务的统计信息")
    public Result getHunterTaskStatistics(@PathVariable("id") Long id, @Valid @RequestBody TaskCondition condition, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            throw new ValidException(bindingResult.getFieldErrors());
        }
        if (!condition.getSelect().equals(TaskConditionSelect.STATE)){
            throw new ValidException("统计选项有误");
        }
        Timestamp[] dc = DateCondition.reset(condition.getBegin(),condition.getEnd());
        List<HunterTask> query = hunterTaskService.findByQueryHunterTaskAndNotPage(QueryHunterTask.init(id,dc[0],dc[1]));
        if (ListUtils.isEmpty(query)){
            throw new DBException("猎刃任务：" + StringResourceCenter.DB_QUERY_FAILED);
        }
        Hunter hunter = query.get(0).getHunter();
        HunterTaskStatistics result = HunterTaskStatistics.statistics(query,condition);
        return Result.init(HunterTaskStatistics.filter(result,condition.getResult()).append(hunter),condition);
    }


    /**
     * 获取猎刃的任务
     * @param queryHunterTask
     * @return
     */
    @PostMapping("/ht/query/{id:\\d+}")
    @ApiOperation(value = "根据查询条件获取指定的猎刃的猎刃任务")
    public Result hunterTasks(@PathVariable("id") Long id,@RequestBody QueryHunterTask queryHunterTask){
        Hunter hunter = hunterService.findOne(id);
        if (hunter == null){
            throw new ValidException("用户不存在");
        }
        queryHunterTask.setHunterId(id);
        Page<HunterTask> query = hunterTaskService.findByQueryHunterTask(queryHunterTask);
        List<HunterTask> result = HunterTask.toIndexAsList(query.getContent());
        TransData trans = new TransData(hunter.getUserId(),hunter.getUser().toTitle(),result);
        return Result.init(trans,queryHunterTask.append(query.getTotalElements(),(long)query.getTotalPages()));
    }


    /**
     * 获取猎刃被评论信息
     * @param queryHunterComment 猎刃评论查询条件
     * @return
     */
    @PostMapping("/comment/{id:\\d+}")
    @ApiOperation(value = "根据猎刃编号获取猎刃的被评论列表")
    public Result getUserComment(@RequestBody QueryHunterComment queryHunterComment,@PathVariable("id") Long id){
        queryHunterComment.setHunterId(id);
        queryHunterComment.setSort(new Sort(Sort.Direction.DESC,CommentHunter.COMMENT_ID));
        Page<CommentHunter> query = commentHunterService.findByQuery(queryHunterComment);
        return Result.init(CommentHunter.toListInIndex(query.getContent()),queryHunterComment.append(query.getTotalElements(),(long)query.getTotalPages()).clearSort());
    }

}
