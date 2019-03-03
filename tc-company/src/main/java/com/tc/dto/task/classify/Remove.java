package com.tc.dto.task.classify;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;


/**
 * 从任务中移除分类
 * @author Cyg
 */
public class Remove {
    private String id;
    /**
     * 分类编号
     */
    @NotEmpty(message = "集合必须存在元素")
    private List<Long> ids;

    public Remove() {
    }

    public Remove(String id, List<Long> ids) {
        this.id = id;
        this.ids = ids;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Long> getIds() {
        return ids;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
    }
}
