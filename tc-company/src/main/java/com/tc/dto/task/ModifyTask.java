package com.tc.dto.task;

import com.tc.db.entity.Task;
import com.tc.db.entity.TaskClassifyRelation;
import com.tc.db.entity.TaskStep;
import com.tc.db.enums.TaskState;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;
import java.util.List;

/**
 * 修改任务
 *
 * @author Cyg
 */
public class ModifyTask {
    @NotEmpty(message = "修改任务的id不可为空")
    private String id;

    @NotEmpty(message = "请设置任务名")
    @Length(max = 20, message = "任务名最大长度为20")
    private String name;

    @NotEmpty(message = "请设置任务的描述信息")
    @Length(max = 240, message = "描述内容不得超过240个字")
    private String context;

    @Size(min = 1, max = 5, message = "至少选择1个分类，小于5个分类")
    private List<Long> classify;

    @Size(min = 1, max = 15, message = "步骤至少要有1步,最多不超过15步")
    private List<AddTaskStep> taskSteps;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public List<Long> getClassify() {
        return classify;
    }

    public void setClassify(List<Long> classify) {
        this.classify = classify;
    }

    public List<AddTaskStep> getTaskSteps() {
        return taskSteps;
    }

    public void setTaskSteps(List<AddTaskStep> taskSteps) {
        this.taskSteps = taskSteps;
    }

    public static Task toTask(ModifyTask modifyTask) {
        Task task = new Task();
        task.setId(modifyTask.id);
        task.setName(modifyTask.name);
        task.setState(TaskState.NEW_CREATE);
        task.setContext(modifyTask.context);
        task.setTaskClassifyRelations(TaskClassifyRelation.toList(task.getId(), modifyTask.classify));
        task.setTaskSteps(TaskStep.toList(task.getId(), modifyTask.taskSteps));
        return task;
    }
}
