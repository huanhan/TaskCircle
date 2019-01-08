package com.tc.controller;

import com.tc.dto.as.*;
import com.tc.dto.user.UserCategoryStatistics;
import com.tc.dto.user.UserTaskStatistics;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 分析与统计管理
 * @author Cyg
 */
@RestController
@ResponseStatus(code = HttpStatus.OK)
@RequestMapping(value = "/as")
public class ASController {

    /**
     * 任务分析
     * @param taskAnalyzeCondition 任务分析条件
     * @param result 结果
     * @return
     */
    @GetMapping("/task")
    @ApiOperation(value = "任务分析")
    public TaskAnalyze getTaskAnalyze(@Valid @RequestBody TaskAnalyzeCondition taskAnalyzeCondition,
                                      BindingResult result){
        return new TaskAnalyze();
    }

    /**
     * 用户分析
     * @param userAnalyzeCondition 用户分析条件
     * @param result 结果
     * @return
     */
    @GetMapping("/user")
    @ApiOperation(value = "用户分析")
    public UserAnalyze getUserAnalyz(@Valid @RequestBody UserAnalyzeCondition userAnalyzeCondition,
                                     BindingResult result){
        return new UserAnalyze();
    }

    /**
     * 财务分析
     * @param financeAnalyzeCondition 财务分析条件
     * @param result 结果
     * @return
     */
    @GetMapping("/finance")
    @ApiOperation(value = "财务分析")
    public FinanceAnalyze getFinanceAnalyz(@Valid @RequestBody FinanceAnalyzeCondition financeAnalyzeCondition,
                                     BindingResult result){
        return new FinanceAnalyze();
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


    /**
     * 根据用户分类获取用户统计信息
     * @param name 用户分类
     * @return
     */
    @GetMapping("/statistics/{name}")
    @ApiOperation(value = "根据用户分类获取用户统计信息")
    public UserCategoryStatistics getUserStatisticsByCategory(@PathVariable("name") String name){
        return new UserCategoryStatistics();
    }




}
