package com.tc.db.repository;

import com.tc.db.entity.TaskClassify;
import com.tc.dto.LongIds;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 评论仓库
 * @author Cyg
 */
public interface TaskClassifyRepository extends JpaRepository<TaskClassify,Long>,JpaSpecificationExecutor<TaskClassify> {

    /**
     * 根据分类名获取父分类获取分类
     * @param name
     * @return
     */
    TaskClassify getFirstByNameEqualsAndParentsIsNull(String name);

    /**
     * 根据分类名和父分类编号获取分类
     * @param name
     * @param parentsId
     * @return
     */
    TaskClassify getFirstByNameEqualsAndParents_IdEquals(String name,Long parentsId);

    /**
     * 根据ID列表删除分类
     * @param ids
     * @return
     */
    @Modifying
    @Query(value = "delete from TaskClassify tc where tc.id in (:ids)")
    int deleteByIds(@Param("ids") List<Long> ids);

    /**
     * 重新设置选择的分类的父分类
     * @param ids
     * @param parents
     * @return
     */
    @Modifying
    @Query(value = "update TaskClassify tc set tc.parents.id = :parents where tc.id in (:ids)")
    int updateByIds(@Param("ids") List<Long> ids, @Param("parents") Long parents);

    /**
     * 根据编号组获取
     * @param ids
     * @return
     */
    List<TaskClassify> findByIdIn(List<Long> ids);

    /**
     * 根据名字数组与父分类获取类别
     * @param names
     * @param id
     * @return
     */
    List<TaskClassify> findByNameIsInAndParentsIdEquals(List<String> names,Long id);

    /**
     * 获取父分类
     * @return
     */
    List<TaskClassify> findByParentsIsNull();

    /**
     * 获取分类详情
     * @param id
     * @return
     */
    TaskClassify findByIdEquals(Long id);

    /**
     * 根据父分类编号获取分类列表
     * @param id
     * @return
     */
    List<TaskClassify> findByParentsIdEquals(Long id);

    /**
     * 删除分类
     * @param id 分类编号
     * @return
     */
    int deleteByIdEquals(Long id);

    /**
     * 获取用户所有任务中使用到的分类
     * @param id
     * @return
     */
    @Query(value =
            "select tc " +
            "from TaskClassify as tc " +
            "where tc.id in ( " +
                "select tcr.taskClassifyId " +
                "from TaskClassifyRelation as tcr " +
                "where tcr.taskId in ( " +
                    "select t " +
                    "from Task as t " +
                    "where t.userId = :id" +
                ")" +
            ")"
    )
    List<TaskClassify> findUserTaskAllClassify(@Param("id") Long id);
}
