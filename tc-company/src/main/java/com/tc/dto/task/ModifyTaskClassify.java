package com.tc.dto.task;

import com.tc.db.entity.TaskClassify;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;


/**
 * 修改分类信息
 * @author Cyg
 */
public class ModifyTaskClassify {

    @NotNull
    @Min(value = 1)
    private Long id;
    @NotBlank
    @Length(max = 6)
    private String name;
    private Long parents;
    @Length(max = 100)
    private String info;
    private String tag;

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

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public static TaskClassify toTaskClassify(TaskClassify taskClassify, ModifyTaskClassify modifyTaskClassify) {
        taskClassify.setName(StringUtils.isEmpty(modifyTaskClassify.name) ? taskClassify.getName() : modifyTaskClassify.getName());
        taskClassify.setParentsId(modifyTaskClassify.getParents());
        taskClassify.setInfo(StringUtils.isEmpty(modifyTaskClassify.info) ? taskClassify.getInfo() : modifyTaskClassify.info);
        taskClassify.setClassifyImg(StringUtils.isEmpty(modifyTaskClassify.tag) ? taskClassify.getClassifyImg() : modifyTaskClassify.tag);
        return taskClassify;
    }
}
