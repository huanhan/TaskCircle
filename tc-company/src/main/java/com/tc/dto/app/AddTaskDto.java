package com.tc.dto.app;

import com.tc.db.entity.Task;
import com.tc.db.entity.TaskClassifyRelation;
import com.tc.db.entity.TaskStep;
import com.tc.db.enums.TaskState;
import com.tc.dto.task.AddTaskStep;
import com.tc.until.IdGenerator;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;
import java.util.List;

public class AddTaskDto {

    @NotEmpty(message = "请设置任务名")
    @Length(max = 20,message = "任务名最大长度为20")
    private String name;

    @NotEmpty(message = "请设置任务的描述信息")
    @Length(max = 240,message = "描述内容不得超过240个字")
    private String context;

    @Size(min=1, max=5,message = "至少选择1个分类，小于5个分类")
    private List<Long> classify;

    @Size(min=1, max=15,message = "步骤至少要有1步,最多不超过15步")
    private List<AddTaskStep> taskSteps;

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

    public static Task toTask(AddTaskDto addTask){
        Task task = new Task();
        task.setId(IdGenerator.INSTANCE.nextId());
        task.setName(addTask.name);
        task.setState(TaskState.NEW_CREATE);
        task.setContext(addTask.context);
        task.setTaskClassifyRelations(TaskClassifyRelation.toList(task.getId(),addTask.classify));
        task.setTaskSteps(TaskStep.toList(task.getId(),addTask.taskSteps));
        return task;
    }
}
