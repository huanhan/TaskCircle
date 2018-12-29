package com.tc.controller;

import com.tc.db.entity.TaskClassify;
import com.tc.dto.Ids;
import com.tc.dto.Task.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * 任务分类控制器
 * @author Cyg
 */
@RestController
@ResponseStatus(code = HttpStatus.OK)
@RequestMapping("/tcl")
public class TaskClassifyController {

    /**
     * 获取任务类别列表
     * @return
     */
    @GetMapping("/all")
    @ApiOperation(value = "获取任务类别列表")
    public List<TaskClassifyResult> all(){
        return new ArrayList<>();
    }

    /**
     * 获取分类详情信息
     * @param id 分类编号
     * @return
     */
    @GetMapping("/{id:\\d+}")
    @ApiOperation(value = "获取分类详情信息")
    public TaskClassify detail(@PathVariable("id") Long id){
        return new TaskClassify();
    }


    /**
     * 根据统计条件获取分类统计详情信息
     * @param id 分类编号
     * @param taskClassifyStatisticsCondition 分类统计条件
     * @param bindingResult 校验异常结果
     * @return
     */
    @GetMapping("/statistics/{id:\\d+}")
    @ApiOperation(value = "根据统计条件获取分类统计详情信息")
    public TaskClassifyStatistics getTaskClassifyStatistics(@PathVariable("id") Long id,
                                                            @Valid @RequestBody TaskClassifyStatisticsCondition taskClassifyStatisticsCondition,
                                                            BindingResult bindingResult){
        return new TaskClassifyStatistics();
    }

    /**
     * 根据分类编号删除分类
     * @param id 分类编号
     */
    @DeleteMapping("/{id:\\d+}")
    @ApiOperation(value = "删除指定分类")
    public void delete(@PathVariable("id") Long id){

    }

    /**
     * 修改分类信息
     * @param modifyTaskClassify 分类信息
     * @param bindingResult 校验异常结果
     * @return
     */
    @PutMapping
    @ApiOperation(value = "修改分类信息")
    public TaskClassify update(@Valid @RequestBody ModifyTaskClassify modifyTaskClassify, BindingResult bindingResult){
        return new TaskClassify();
    }

    /**
     * 添加新分类
     * @param addTaskClassify 分类信息
     * @param bindingResult 校验异常结果
     * @return
     */
    @PostMapping
    @ApiOperation(value = "添加新分类")
    public TaskClassify add(@Valid @RequestBody AddTaskClassify addTaskClassify, BindingResult bindingResult){
        return new TaskClassify();
    }

    /**
     * 修改子分类的父分类
     * @param pid 父分类编号
     * @param ids 子分类列表
     */
    @PutMapping("/parent/{pid:\\d+}")
    @ApiOperation(value = "修改子分类的父分类")
    public void modifyClassifyParent(@PathVariable("pid") Long pid,@RequestBody Ids ids){

    }

    
}
