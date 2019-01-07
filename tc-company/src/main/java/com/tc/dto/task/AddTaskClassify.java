package com.tc.dto.task;

import com.tc.db.entity.Admin;
import com.tc.db.entity.TaskClassify;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;


/**
 * 添加分类
 * @author Cyg
 */
public class AddTaskClassify {

    @NotBlank
    @Length(max = 6)
    private String name;
    @NotNull
    private Long creation;
    private Long parents;
    @Length(max = 100)
    private String info;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCreation() {
        return creation;
    }

    public void setCreation(Long creation) {
        this.creation = creation;
    }

    public Long getParents() {
        return parents;
    }

    public void setParents(Long parents) {
        this.parents = parents;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public static TaskClassify toTaskClassify(AddTaskClassify addTaskClassify){
        TaskClassify taskClassify = new TaskClassify();
        taskClassify.setCreationId(addTaskClassify.creation);
        taskClassify.setParentsId(addTaskClassify.parents);
        taskClassify.setInfo(addTaskClassify.getInfo());
        taskClassify.setName(addTaskClassify.getName());
        return taskClassify;
    }
}
