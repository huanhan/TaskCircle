package com.tc.service.impl;

import com.tc.db.entity.Task;
import com.tc.db.entity.TaskClassify;
import com.tc.db.entity.TaskClassifyRelation;
import com.tc.db.repository.TaskClassifyRelationRepository;
import com.tc.db.repository.TaskClassifyRepository;
import com.tc.db.repository.TaskRepository;
import com.tc.dto.LongIds;
import com.tc.dto.task.QueryTaskClassify;
import com.tc.exception.DBException;
import com.tc.exception.ValidException;
import com.tc.service.TaskClassifyService;
import com.tc.until.ListUtils;
import com.tc.until.StringResourceCenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.util.List;

/**
 * 任务分类服务的实现
 * @author Cyg
 */
@Service
public class TaskClassifyServiceImpl extends AbstractBasicServiceImpl<TaskClassify> implements TaskClassifyService {

    @Autowired
    private TaskClassifyRepository taskClassifyRepository;

    @Autowired
    private TaskClassifyRelationRepository taskClassifyRelationRepository;

    @Transactional(rollbackFor = RuntimeException.class,readOnly = true)
    @Override
    public Page<TaskClassify> queryByQueryTaskClassify(QueryTaskClassify queryTaskClassify) {
        return taskClassifyRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = QueryTaskClassify.initPredicates(queryTaskClassify,root,query,cb);

            return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        },queryTaskClassify);
    }

    @Transactional(rollbackFor = RuntimeException.class,readOnly = true)
    @Override
    public boolean whether(String name, Long parents) {
        TaskClassify taskClassify;

        if (parents == null){
            taskClassify = taskClassifyRepository.getFirstByNameEqualsAndParentsIsNull(name);
        }else {
            taskClassify = taskClassifyRepository.getFirstByNameEqualsAndParents_IdEquals(name,parents);
        }

        return taskClassify == null;
    }

    @Transactional(rollbackFor = RuntimeException.class,readOnly = true)
    @Override
    public List<TaskClassify> findByIds(List<Long> ids) {
        return taskClassifyRepository.findByIdIn(ids);
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public List<Long> moveChildren(Long pid, List<Long> ids) {

        //获取用户传入的分类编号的具体内容
        List<TaskClassify> queryTcs = this.findByIds(ids);
        if (ListUtils.isEmpty(queryTcs)){
            throw new ValidException(StringResourceCenter.VALIDATOR_QUERY_FAILED);
        }

        //与其他父分类的子分类的名称进行比对判断，获得不重复的子分类
        List<String> names = TaskClassify.toNames(queryTcs);
        List<TaskClassify> whetherList = this.whetherByNames(names,pid);
        if (!ListUtils.isEmpty(whetherList)){
            whetherList.forEach(tc -> queryTcs.removeIf(qtc -> tc.getName().equals(qtc.getName())));
        }

        //重新设置分类的父分类
        List<Long> result = TaskClassify.toIds(queryTcs);
        int count = taskClassifyRepository.updateByIds(result,pid);

        if (result.size() != count){
            throw new DBException(StringResourceCenter.DB_INSERT_ABNORMAL);
        }

        return result;
    }

    @Transactional(rollbackFor = RuntimeException.class,readOnly = true)
    @Override
    public List<TaskClassify> whetherByNames(List<String> names, Long pid) {
        return taskClassifyRepository.findByNameIsInAndParentsIdEquals(names,pid);
    }

    @Transactional(rollbackFor = RuntimeException.class,readOnly = true)
    @Override
    public List<TaskClassify> findByParents() {
        return taskClassifyRepository.findByParentsIsNull();
    }

    @Transactional(rollbackFor = RuntimeException.class,readOnly = true)
    @Override
    public TaskClassify findOne(Long id) {
        TaskClassify result = taskClassifyRepository.findOne(id);
//        List<TaskClassifyRelation> queryTcrs = taskClassifyRelationRepository.findByTaskClassifyIdEquals(id);
//        if (!ListUtils.isEmpty(queryTcrs)) {
//            result.setTaskClassifyRelations(queryTcrs);
//        }
//        if (result.getParents() == null){
//            List<TaskClassify> queryTcs = taskClassifyRepository.findByParentsIdEquals(result.getId());
//            if (!ListUtils.isEmpty(queryTcs)){
//                result.setTaskClassifies(queryTcs);
//            }
//        }
        return result;
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public boolean deleteById(Long id) {
        int count = taskClassifyRepository.deleteByIdEquals(id);
        return count > 0;
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public TaskClassify save(TaskClassify taskClassify) {
        return taskClassifyRepository.saveAndFlush(taskClassify);
    }

    @Override
    public boolean deleteByIds(LongIds ids) {
        int count = taskClassifyRepository.deleteByIds(ids.getIds());
        return count > 0;
    }
}
