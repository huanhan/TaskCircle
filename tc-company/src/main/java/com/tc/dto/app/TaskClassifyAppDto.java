package com.tc.dto.app;

import java.util.List;

public class TaskClassifyAppDto {

    private Long id;
    private String name;
    private String classifyImg;
    private List<TaskClassifyAppDto> taskClassifies;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassifyImg() {
        return classifyImg;
    }

    public void setClassifyImg(String classifyImg) {
        this.classifyImg = classifyImg;
    }

    public List<TaskClassifyAppDto> getTaskClassifies() {
        return taskClassifies;
    }

    public void setTaskClassifies(List<TaskClassifyAppDto> taskClassifies) {
        this.taskClassifies = taskClassifies;
    }
}
