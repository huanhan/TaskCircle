package com.tc.controller;

import com.tc.db.entity.*;
import com.tc.dto.as.*;
import com.tc.dto.enums.TaskConditionSelect;
import com.tc.dto.finance.QueryFinance;
import com.tc.dto.statistics.TaskCondition;
import com.tc.dto.task.QueryHunterTask;
import com.tc.dto.task.QueryTask;
import com.tc.dto.user.*;
import com.tc.exception.DBException;
import com.tc.exception.ValidException;
import com.tc.service.HunterTaskService;
import com.tc.service.TaskClassifyService;
import com.tc.service.TaskService;
import com.tc.service.UserWithdrawService;
import com.tc.until.ListUtils;
import com.tc.until.StringResourceCenter;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 分析与统计管理
 * @author Cyg
 */
@RestController
@ResponseStatus(code = HttpStatus.OK)
@RequestMapping(value = "/as")
public class ASController {



    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskClassifyService taskClassifyService;

    @Autowired
    private HunterTaskService hunterTaskService;

    @Autowired
    private UserWithdrawService userWithdrawService;
    /**
     * 获取所有用户任务的统计信息
     * @return
     */
    @PostMapping("/user/task")
    @ApiOperation(value = "获取所有用户任务的统计信息")
    public UserTaskStatistics taskStatistics(@Valid @RequestBody TaskCondition condition,BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            throw new ValidException(bindingResult.getFieldErrors());
        }
        List<Task> query = taskService.findByQueryTaskAndNotPage(QueryTask.init(condition.getBegin(),condition.getEnd()));
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
     * 获取猎刃接受的任务的统计信息
     * @param id 用户编号
     * @return
     */
    @PostMapping("/hunter/task")
    @ApiOperation(value = "获取猎刃接受的任务的统计信息")
    public HunterTaskStatistics getHunterTaskStatistics(@PathVariable("id") Long id, @Valid @RequestBody TaskCondition condition, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            throw new ValidException(bindingResult.getFieldErrors());
        }
        if (!condition.getSelect().equals(TaskConditionSelect.STATE)){
            throw new ValidException("统计选项有误");
        }
        List<HunterTask> query = hunterTaskService.findByQueryHunterTaskAndNotPage(QueryHunterTask.init(condition.getBegin(),condition.getEnd()));
        if (ListUtils.isEmpty(query)){
            throw new DBException("猎刃任务：" + StringResourceCenter.DB_QUERY_FAILED);
        }
        HunterTaskStatistics result = HunterTaskStatistics.statistics(query,condition);
        return HunterTaskStatistics.filter(result,condition.getResult());
    }

    /**
     * 打印报告
     * @param context
     */
    @GetMapping("/print")
    @ApiOperation(value = "打印报告")
    public void printReport(@RequestBody String context){

    }

    /**
     * 保存报告
     * @param context
     */
    @GetMapping("/save")
    @ApiOperation(value = "保存报告到Word")
    public void saveReportToWPF(@RequestBody String context){

    }





}
