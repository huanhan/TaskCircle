package com.tc.dto.app;


import java.util.List;

public class HomeAppDto {

    private List<String> banners;

    private List<TaskClassifyAppDto> taskClassifyAppDtos;

    private List<TaskAppDto> taskAppDtos;

    public List<String> getBanners() {
        return banners;
    }

    public void setBanners(List<String> banners) {
        this.banners = banners;
    }

    public List<TaskClassifyAppDto> getTaskClassifyAppDtos() {
        return taskClassifyAppDtos;
    }

    public void setTaskClassifyAppDtos(List<TaskClassifyAppDto> taskClassifyAppDtos) {
        this.taskClassifyAppDtos = taskClassifyAppDtos;
    }

    public List<TaskAppDto> getTaskAppDtos() {
        return taskAppDtos;
    }

    public void setTaskAppDtos(List<TaskAppDto> taskAppDtos) {
        this.taskAppDtos = taskAppDtos;
    }
}
