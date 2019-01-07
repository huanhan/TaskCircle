package com.tc.controller;

import com.tc.db.entity.TaskClassify;
import com.tc.dto.LongIds;
import com.tc.dto.Result;
import com.tc.dto.Show;
import com.tc.dto.task.*;
import com.tc.exception.DBException;
import com.tc.exception.ValidException;
import com.tc.service.TaskClassifyRelationService;
import com.tc.service.TaskClassifyService;
import com.tc.until.ListUtils;
import com.tc.until.StringResourceCenter;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

    @Autowired
    private TaskClassifyService taskClassifyService;

    @Autowired
    private TaskClassifyRelationService taskClassifyRelationService;

    /**
     * 获取任务类别列表
     * @return
     */
    @PostMapping("/query")
    @ApiOperation(value = "获取任务类别列表")
    public Result all(@RequestBody QueryTaskClassify queryTaskClassify){
        Page<TaskClassify> queryTcs = taskClassifyService.queryByQueryTaskClassify(queryTaskClassify);
        return Result.init(TaskClassify.toListInIndex(queryTcs.getContent()),queryTaskClassify);
    }

    /**
     * 获取分类详情信息
     * @param id 分类编号
     * @return
     */
    @GetMapping("/detail/{id:\\d+}")
    @ApiOperation(value = "获取分类详情信息")
    public TaskClassify detail(@PathVariable("id") Long id){
        TaskClassify queryTc = taskClassifyService.findOne(id);
        return TaskClassify.reset(queryTc);
    }

    /**
     * 根据分类编号删除分类
     * @param id 分类编号
     */
    @DeleteMapping("/{id:\\d+}")
    @ApiOperation(value = "删除指定分类")
    public void delete(@PathVariable("id") Long id){
        boolean delIsSuccess = taskClassifyService.deleteById(id);
        if (!delIsSuccess){throw new DBException(StringResourceCenter.DB_DELETE_ABNORMAL);}
    }

    /**
     * 根据分类编号组删除分类
     * @param ids 分类编号组
     */
    @PostMapping("/delete/select")
    @ApiOperation(value = "删除选择的分类")
    public void delete(@RequestBody LongIds ids){
        boolean delIsSuccess = taskClassifyService.deleteByIds(ids);
        if (!delIsSuccess){throw new DBException(StringResourceCenter.DB_DELETE_ABNORMAL);}
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
        if (bindingResult.hasErrors()){
            throw new ValidException(bindingResult.getFieldErrors());
        }

        boolean whether = taskClassifyService.whether(addTaskClassify.getName(),addTaskClassify.getParents());
        if (!whether){
            throw new ValidException(StringResourceCenter.VALIDATOR_INSERT_ABNORMAL);
        }

        TaskClassify result = taskClassifyService.save(AddTaskClassify.toTaskClassify(addTaskClassify));
        return TaskClassify.reset(result);
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
        if (bindingResult.hasErrors()){
            throw new ValidException(bindingResult.getFieldErrors());
        }

        TaskClassify queryTc = taskClassifyService.findOne(modifyTaskClassify.getId());
        if (queryTc == null){
            throw new DBException(StringResourceCenter.DB_QUERY_ABNORMAL);
        }


        if (!modifyTaskClassify.getName().equals(queryTc.getName())){
            boolean whether;
            whether = taskClassifyService.whether(modifyTaskClassify.getName(),modifyTaskClassify.getParents());
            if (!whether){
                throw new ValidException(StringResourceCenter.VALIDATOR_UPDATE_ABNORMAL);
            }
        }

        TaskClassify result = taskClassifyService.save(ModifyTaskClassify.toTaskClassify(queryTc,modifyTaskClassify));
        return TaskClassify.reset(result);
    }

    @GetMapping("/parents")
    @ApiOperation(value = "获取所有父分类")
    public List<Show> parents(){
        List<TaskClassify> queryTcs = taskClassifyService.findByParents();
        return TaskClassify.toShows(queryTcs);
    }

    /**
     * 修改子分类的父分类
     * @param ids 子分类列表(包含父分类编号)
     */
    @PutMapping("/move/children")
    @ApiOperation(value = "修改子分类的父分类")
    public Result modifyClassifyParent(@Valid @RequestBody LongIds ids,BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            throw new ValidException(bindingResult.getFieldErrors());
        }

        //移动子分类，并返回移动成功的子分类编号
        List<Long> result = taskClassifyService.moveChildren(ids.getId(),ids.getIds());


        return Result.init(result);
    }

    /**
     * 从分类中移除所属的任务
     * @param ids 任务编号列表
     * @param bindingResult 校验异常结果
     */
    @PostMapping("/delete/select/task")
    @ApiOperation(value = "从分类中移除所属的任务")
    public void removeTask(@Valid @RequestBody LongIds ids, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            throw new ValidException(bindingResult.getFieldErrors());
        }
        boolean delIsSuccess = taskClassifyRelationService.deleteByIds(ids);
        if (!delIsSuccess){throw new DBException(StringResourceCenter.DB_DELETE_ABNORMAL);}
    }


}
