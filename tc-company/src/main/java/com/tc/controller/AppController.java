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
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

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

        bannsers.add("https://fuss10.elemecdn.com/7/90/8012dcb83434050f0363316f24809jpeg.jpeg?imageMogr/format/webp/thumbnail/568x/");
        bannsers.add("https://fuss10.elemecdn.com/d/f5/456ea6285819f7932d8606bffeb35jpeg.jpeg?imageMogr/format/webp/thumbnail/568x/");
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
        QueryTask queryTask = new QueryTask();
        queryTask.setState(TaskState.ISSUE);
        List<Task> taskList = taskService.findByQueryTask(queryTask).getContent();
        for (Task task : taskList) {
            taskAppDtos.add(TaskAppDto.toDetail(task));
        }
        homeAppDto.setTaskAppDtos(taskAppDtos);


        return homeAppDto;
    }


}
