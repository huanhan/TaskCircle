package com.tc.controller;

import com.tc.db.entity.TaskClassify;
import com.tc.dto.LongIds;
import com.tc.dto.Result;
import com.tc.dto.Show;
import com.tc.dto.StringIds;
import com.tc.dto.task.AddTaskClassify;
import com.tc.dto.task.ModifyTaskClassify;
import com.tc.dto.task.QueryTaskClassify;
import com.tc.exception.DBException;
import com.tc.exception.ValidException;
import com.tc.service.TaskClassifyRelationService;
import com.tc.service.TaskClassifyService;
import com.tc.until.ListUtils;
import com.tc.until.StringResourceCenter;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
        List<TaskClassify> queryTcs = taskClassifyService.queryByQueryAndNotPage(queryTaskClassify);
        return Result.init(TaskClassify.reset(queryTcs),queryTaskClassify);
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
    public void delete(HttpServletRequest request,@PathVariable("id") Long id){
        Long me = Long.parseLong(request.getAttribute(StringResourceCenter.USER_ID).toString());
        TaskClassify taskClassify = taskClassifyService.findOne(id);
        if (!me.equals(taskClassify.getCreationId())){
            throw new ValidException(StringResourceCenter.VALIDATOR_AUTHORITY_FAILED);
        }
        if (taskClassify.getParents() == null){
            if (ListUtils.isNotEmpty(taskClassify.getTaskClassifies())){
                throw new ValidException("存在子分类，不允许删除");
            }
        }
        boolean delIsSuccess = taskClassifyService.deleteById(id);
        if (!delIsSuccess){throw new DBException(StringResourceCenter.DB_DELETE_ABNORMAL);}
    }

    /**
     * 根据分类编号组删除分类
     * @param ids 分类编号组
     */
    @PostMapping("/delete/select")
    @ApiOperation(value = "删除选择的分类")
    public List<Long> delete(HttpServletRequest request,@RequestBody LongIds ids){
        Long me = Long.parseLong(request.getAttribute(StringResourceCenter.USER_ID).toString());
        List<TaskClassify> result = taskClassifyService.findByIds(ids.getIds());
        result.removeIf(taskClassify -> !me.equals(taskClassify.getCreationId()));
        if (ListUtils.isNotEmpty(result)){
            List<Long> children = new ArrayList<>();
            List<Long> parents = new ArrayList<>();
            result.forEach(taskClassify -> {
                if (taskClassify.getParents() != null){
                    children.add(taskClassify.getId());
                }else {
                    if (ListUtils.isEmpty(taskClassify.getTaskClassifies())){
                        parents.add(taskClassify.getId());
                    }
                }
            });
            List<Long> del = new ArrayList<>();
            if (ListUtils.isNotEmpty(children)){
                del.addAll(children);
            }
            if (ListUtils.isNotEmpty(parents)){
                del.addAll(parents);
            }
            if (ListUtils.isNotEmpty(del)){
                boolean delIsSuccess = taskClassifyService.deleteByIds(new LongIds(0L,del));
                if (!delIsSuccess){throw new DBException(StringResourceCenter.DB_DELETE_ABNORMAL);}
                else {return del;}
            }else {
                throw new ValidException("注意父分类，存在子分类时不允许删除");
            }
        }else {
            throw new ValidException(StringResourceCenter.VALIDATOR_AUTHORITY_FAILED);
        }



    }

    /**
     * 添加新分类
     * @param addTaskClassify 分类信息
     * @param bindingResult 校验异常结果
     * @return
     */
    @PostMapping
    @ApiOperation(value = "添加新分类")
    public TaskClassify add(HttpServletRequest request, @Valid @RequestBody AddTaskClassify addTaskClassify, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            throw new ValidException(bindingResult.getFieldErrors());
        }
        Long id = Long.parseLong(request.getAttribute(StringResourceCenter.USER_ID).toString());
        addTaskClassify.setCreation(id);
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
    public TaskClassify update(HttpServletRequest request, @Valid @RequestBody ModifyTaskClassify modifyTaskClassify, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            throw new ValidException(bindingResult.getFieldErrors());
        }

        TaskClassify queryTc = taskClassifyService.findOne(modifyTaskClassify.getId());
        if (queryTc == null){
            throw new DBException(StringResourceCenter.DB_QUERY_ABNORMAL);
        }
        Long id = Long.parseLong(request.getAttribute(StringResourceCenter.USER_ID).toString());
        if (!queryTc.getCreationId().equals(id)){
            throw new ValidException(StringResourceCenter.VALIDATOR_AUTHORITY_FAILED);
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
    public void removeTask(@Valid @RequestBody StringIds ids, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            throw new ValidException(bindingResult.getFieldErrors());
        }
        boolean delIsSuccess = taskClassifyRelationService.deleteByIds(ids);
        if (!delIsSuccess){throw new DBException(StringResourceCenter.DB_DELETE_ABNORMAL);}
    }


}
