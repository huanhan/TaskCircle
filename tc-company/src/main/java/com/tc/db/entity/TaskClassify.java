package com.tc.db.entity;

import com.tc.dto.Show;
import com.tc.until.ListUtils;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Cyg
 * 任务分类实体
 */
@Entity
@Table(name = "task_classify", schema = "tc-company")
public class TaskClassify implements Serializable {

    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String CLASSIFY_IMG = "classifyImg";
    public static final String INFO = "info";
    public static final String CREATION = "creation";
    public static final String CREATE_TIME = "createTime";
    public static final String PARENTS = "parents";
    public static final String TASK_CLASSIFY_RELATIONS = "taskClassifyRelations";

    private Long id;
    private String name;
    private Long parentsId;
    private Long creationId;
    private String classifyImg;
    private Admin creation;
    private Timestamp createTime;
    private TaskClassify parents;
    private String info;
    private Collection<TaskClassifyRelation> taskClassifyRelations;
    private Collection<TaskClassify> taskClassifies;

    private Integer taskNum = 0;
    private Integer childNum = 0;

    public TaskClassify() {
    }

    public TaskClassify(Long id) {
        this.id = id;
    }

    public TaskClassify(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "parents_id")
    public Long getParentsId() {
        return parentsId;
    }

    public void setParentsId(Long parentsId) {
        this.parentsId = parentsId;
    }

    @Basic
    @Column(name = "creation")
    public Long getCreationId() {
        return creationId;
    }

    public void setCreationId(Long creationId) {
        this.creationId = creationId;
    }

    @Basic
    @Column(name = "classify_img")
    public String getClassifyImg() {
        return classifyImg;
    }

    public void setClassifyImg(String classifyImg) {
        this.classifyImg = classifyImg;
    }

    @Basic
    @CreationTimestamp
    @Column(name = "create_time")
    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @ManyToOne
    @JoinColumn(name = "parents_id",referencedColumnName = "id",insertable = false,updatable = false)
    public TaskClassify getParents() {
        return parents;
    }

    public void setParents(TaskClassify parents) {
        this.parents = parents;
    }

    @Basic
    @Column(name = "info")
    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    @ManyToOne
    @JoinColumn(name = "creation",referencedColumnName = "user_id",nullable = false,insertable = false,updatable = false)
    public Admin getCreation() {
        return creation;
    }

    public void setCreation(Admin creation) {
        this.creation = creation;
    }

    @OneToMany(mappedBy = "taskClassify")
    public Collection<TaskClassifyRelation> getTaskClassifyRelations() {
        return taskClassifyRelations;
    }

    public void setTaskClassifyRelations(Collection<TaskClassifyRelation> taskClassifyRelationsById) {
        this.taskClassifyRelations = taskClassifyRelationsById;
    }

    @OneToMany(mappedBy = "parents")
    public Collection<TaskClassify> getTaskClassifies() {
        return taskClassifies;
    }

    public void setTaskClassifies(Collection<TaskClassify> taskClassifies) {
        this.taskClassifies = taskClassifies;
    }

    @Transient
    public Integer getTaskNum() {
        return taskNum;
    }

    public void setTaskNum(Integer taskNum) {
        this.taskNum = taskNum;
    }

    @Transient
    public Integer getChildNum() {
        return childNum;
    }

    public void setChildNum(Integer childNum) {
        this.childNum = childNum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {return true;}
        if (o == null || getClass() != o.getClass()) {return false;}
        TaskClassify that = (TaskClassify) o;
        return id.equals(that.getId()) &&
                creationId.equals(that.creationId) &&
                parentsId.equals(that.parentsId) &&
                Objects.equals(name, that.name) &&
                Objects.equals(createTime, that.createTime) &&
                Objects.equals(info, that.info);
    }



    @Override
    public int hashCode() {
        return Objects.hash(id, name, createTime, creation, parentsId, creationId, info);
    }

    public static List<TaskClassify> toListInIndex(List<TaskClassify> content) {
        if (!ListUtils.isEmpty(content)){
            content.forEach(parent -> {
                parent.setCreation(null);
                parent.setInfo(null);
                parent.setTaskClassifyRelations(null);
                if (parent.getParents() != null){
                    parent.setParents(new TaskClassify(parent.getParents().getId(),parent.getParents().getName()));
                }else {
                    if (!ListUtils.isEmpty(parent.getTaskClassifies())) {
                        List<TaskClassify> list = new ArrayList<>(parent.getTaskClassifies());
                        parent.setTaskClassifies(toListInIndex(list));
                    }
                }
            });
        }
        return content;
    }

    public static List<TaskClassify> reset(List<TaskClassify> content){
        List<TaskClassify> indexList = toListInIndex(content);
        if (!ListUtils.isEmpty(indexList)){
            List<TaskClassify> parents = new ArrayList<>();
            List<TaskClassify> children = new ArrayList<>();
            indexList.forEach(tc ->{
                if (tc.getParents() == null){
                    parents.add(tc);
                }else {
                    children.add(tc);
                }
            });

            //从孩子中移除父亲中已有的孩子
            parents.forEach(p -> p.getTaskClassifies().forEach(c -> children.removeIf(cc -> c.getId().equals(cc.getId()))));

            //遍历没有挂载在父亲列表中的孩子，根据孩子已有父亲挂载到父亲列表中
            if (!ListUtils.isEmpty(children)){
                children.forEach(c ->{
                    AtomicBoolean hasLike = new AtomicBoolean(false);
                    parents.forEach(p ->{
                        if (c.getParentsId().equals(p.getId())){
                            hasLike.set(true);
                            p.getTaskClassifies().add(c);
                        }
                    });
                    if (!hasLike.get()){
                        //反转两者的主次关系
                        TaskClassify p = c.getParents();
                        List<TaskClassify> tcs = new ArrayList<>();
                        c.setParents(null);
                        tcs.add(c);
                        p.setTaskClassifies(tcs);
                        parents.add(p);
                    }
                    hasLike.set(false);
                });
            }

            return parents;
        }
        return indexList;
    }


    public static TaskClassify reset(TaskClassify taskClassify) {
        if (taskClassify != null){
            if (taskClassify.getParents() != null) {
                taskClassify.setParents(new TaskClassify(taskClassify.getParents().id, taskClassify.getParents().name));
            }

            if (!ListUtils.isEmpty(taskClassify.getTaskClassifies())){
                taskClassify.getTaskClassifies().forEach(child -> taskClassify.setChildNum(taskClassify.getChildNum() + 1));
                taskClassify.setTaskClassifies(null);
            }
            if (!ListUtils.isEmpty(taskClassify.getTaskClassifyRelations())){
                taskClassify.getTaskClassifyRelations().forEach(tcr -> taskClassify.setTaskNum(taskClassify.getTaskNum() + 1));
                taskClassify.setTaskClassifyRelations(null);
            }
            if (taskClassify.getCreation() != null){
                Admin admin;
                if (taskClassify.getCreation().getUser() != null){
                    admin = new Admin(taskClassify.getCreation().getUserId(),
                            taskClassify.getCreation().getUser().getName(),
                            taskClassify.getCreation().getUser().getUsername());
                }else {
                    admin = new Admin(taskClassify.getCreation().getUserId());
                }
                taskClassify.setCreation(admin);
            }
        }
        return taskClassify;
    }

    public static List<String> toNames(List<TaskClassify> queryTcs) {
        List<String> names = new ArrayList<>();
        if (!ListUtils.isEmpty(queryTcs)){
            queryTcs.forEach(tc -> names.add(tc.name));
        }
        return names;
    }

    public static List<Long> toIds(List<TaskClassify> queryTcs) {
        List<Long> ids = new ArrayList<>();
        if (!ListUtils.isEmpty(queryTcs)){
            queryTcs.forEach(tc -> ids.add(tc.getId()));
        }
        return ids;
    }

    public static List<Show> toShows(List<TaskClassify> queryTcs) {
        List<Show> result = new ArrayList<>();
        if (!ListUtils.isEmpty(queryTcs)){
            queryTcs.forEach(tc -> result.add(new Show(tc.id,tc.name)));
        }
        return result;
    }
}
