package com.tc.controller;

import com.tc.db.entity.Task;
import com.tc.dto.Task.QueryTask;
import com.tc.dto.Task.TaskClassifyResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * 任务控制器
 * @author Cyg
 */
@RestController
@ResponseStatus(code = HttpStatus.OK)
@RequestMapping("/task")
public class TaskController {

    /**
     * 根据查询条件获取任务列表
     * @param queryTask 查询条件
     * @param bindingResult 校验异常结果
     * @return
     */
    @GetMapping("/all")
    @ApiOperation(value = "获取任务类别列表")
    public List<Task> all(@Valid @RequestBody QueryTask queryTask, BindingResult bindingResult){
        return new ArrayList<>();
    }
}
