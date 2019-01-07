package com.tc.service;

import com.tc.db.entity.TaskClassify;
import com.tc.dto.task.QueryTaskClassify;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 评论仓库
 * @author Cyg
 */
public interface TaskClassifyService extends BasicService<TaskClassify> {

    /**
     * 根据查询条件获取分类
     * @param queryTaskClassify
     * @return
     */
    Page<TaskClassify> queryByQueryTaskClassify(QueryTaskClassify queryTaskClassify);

    /**
     * 判断是否存在相同分类
     * @param name
     * @param parents
     * @return
     */
    boolean whether(String name, Long parents);

    /**
     * 移动分类到指定的父分类下
     * @param pid
     * @param ids
     * @return
     */
    List<Long> moveChildren(Long pid, List<Long> ids);

    /**
     * 根据分类名与父分类查询
     * @param names
     * @param pid
     * @return
     */
    List<TaskClassify> whetherByNames(List<String> names, Long pid);

    /**
     * 获取所有父分类
     * @return
     */
    List<TaskClassify> findByParents();
}
