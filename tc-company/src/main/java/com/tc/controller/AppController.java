package com.tc.controller;

import com.tc.db.entity.Task;
import com.tc.db.entity.TaskClassify;
import com.tc.db.enums.TaskState;
import com.tc.dto.app.HomeAppDto;
import com.tc.dto.app.TaskAppDto;
import com.tc.dto.app.TaskClassifyAppDto;
import com.tc.dto.task.QueryTask;
import com.tc.service.TaskClassifyService;
import com.tc.service.TaskService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@ResponseStatus(code = HttpStatus.OK)
@RequestMapping(value = "/app")
public class AppController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskClassifyService taskClassifyService;


    @GetMapping("/index")
    @ApiOperation(value = "获取首页信息")
    public HomeAppDto index() {
        HomeAppDto homeAppDto = new HomeAppDto();
        ArrayList<String> bannsers = new ArrayList<>();

        bannsers.add("http://src.taskcircle.lrvik.xin/banner1.png");
        bannsers.add("http://src.taskcircle.lrvik.xin/banner2.png");
        homeAppDto.setBanners(bannsers);

        //拷贝所有分类
        List<TaskClassify> taskClassifys = taskClassifyService.findByParents();
        List<TaskClassifyAppDto> taskClassifyAppDtos = new ArrayList<>();
        TaskClassifyAppDto taskClassifyAppDto;
        for (TaskClassify taskClassify : taskClassifys) {
            taskClassifyAppDto = new TaskClassifyAppDto();
            BeanUtils.copyProperties(taskClassify, taskClassifyAppDto);
            taskClassifyAppDtos.add(taskClassifyAppDto);
        }
        homeAppDto.setTaskClassifyAppDtos(taskClassifyAppDtos);


        //拷贝所有任务
        List<TaskAppDto> taskAppDtos = new ArrayList<>();
        QueryTask queryTask = new QueryTask(0, 20);
        queryTask.setState(TaskState.ISSUE);
        List<Task> taskList = taskService.findByQueryTask(queryTask).getContent();
        for (Task task : taskList) {
            taskAppDtos.add(TaskAppDto.toDetail(task));
        }
        homeAppDto.setTaskAppDtos(taskAppDtos);


        return homeAppDto;
    }

    @GetMapping("/task/{sort}/{lat}/{log}/{id:\\d+}")
    @ApiOperation(value = "获取距离最近的任务")
    public List<TaskAppDto> taskByDistance(@PathVariable("sort") String sort,
                                           @PathVariable("lat") Double lat,
                                           @PathVariable("log") Double log,
                                           @PathVariable("id") Long id) {
        List<Task> content=null;
        QueryTask queryTask = new QueryTask(0, 20);
        queryTask.setState(TaskState.ISSUE);
        switch (sort) {
            case "def":
                content = taskService.findByQueryTask(queryTask).getContent();
                break;
            case "distance":
                content = taskService.taskByDistance(lat, log);
                break;
            case "price":
                queryTask.setSort(new Sort(Sort.Direction.DESC, Task.ORIGINALMONEY));
                content = taskService.findByQueryTask(queryTask).getContent();
                break;
            case "time":
                queryTask.setSort(new Sort(Sort.Direction.DESC, Task.CREATE_TIME));
                content = taskService.findByQueryTask(queryTask).getContent();
                break;
        }

        List<TaskAppDto> taskAppDtos = new ArrayList<>();
        for (Task task : content) {
            taskAppDtos.add(TaskAppDto.toDetail(task));
        }
        return taskAppDtos;
    }
}
