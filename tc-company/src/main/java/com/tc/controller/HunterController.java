package com.tc.controller;

import com.tc.db.entity.Hunter;
import com.tc.db.entity.HunterTask;
import com.tc.db.entity.User;
import com.tc.db.enums.UserCategory;
import com.tc.dto.Result;
import com.tc.dto.enums.TaskConditionSelect;
import com.tc.dto.statistics.TaskCondition;
import com.tc.dto.task.QueryHunterTask;
import com.tc.dto.user.HunterTaskStatistics;
import com.tc.dto.user.QueryUser;
import com.tc.dto.user.hunter.QueryHunter;
import com.tc.exception.DBException;
import com.tc.exception.ValidException;
import com.tc.service.HunterService;
import com.tc.service.HunterTaskService;
import com.tc.until.ListUtils;
import com.tc.until.StringResourceCenter;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
        List<Hunter> result = Hunter.toIndexAsList(queryHunters.getContent());
        return Result.init(result,queryHunter);

    }

    /**
     * 根据用户编号获取猎刃接受的任务的统计信息
     * @param id 用户编号
     * @return
     */
    @PostMapping("/statistics/{id:\\d+}/in/task")
    @ApiOperation(value = "根据用户编号获取猎刃接受的任务的统计信息")
    public HunterTaskStatistics getHunterTaskStatistics(@PathVariable("id") Long id, @Valid @RequestBody TaskCondition condition, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            throw new ValidException(bindingResult.getFieldErrors());
        }
        if (!condition.getSelect().equals(TaskConditionSelect.STATE)){
            throw new ValidException("统计选项有误");
        }
        List<HunterTask> query = hunterTaskService.findByQueryHunterTaskAndNotPage(QueryHunterTask.init(id,condition.getBegin(),condition.getEnd()));
        if (ListUtils.isEmpty(query)){
            throw new DBException("猎刃任务：" + StringResourceCenter.DB_QUERY_FAILED);
        }
        HunterTaskStatistics result = HunterTaskStatistics.statistics(query,condition);
        return HunterTaskStatistics.filter(result,condition.getResult());
    }


    /**
     * 获取猎刃的任务
     * @param queryHunterTask
     * @return
     */
    @PostMapping("/ht/query")
    @ApiOperation(value = "根据查询条件获取指定的猎刃的猎刃任务")
    public Result hunterTasks(@RequestBody QueryHunterTask queryHunterTask){
        if (queryHunterTask.getHunterId() == null){
            throw new ValidException("猎刃编号不允许为空");
        }
        Page<HunterTask> query = hunterTaskService.findByQueryHunterTask(queryHunterTask);
        return Result.init(HunterTask.toIndexAsList(query.getContent()),queryHunterTask);
    }


}
