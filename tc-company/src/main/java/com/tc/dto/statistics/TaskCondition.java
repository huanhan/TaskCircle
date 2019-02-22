package com.tc.dto.statistics;

import com.tc.dto.enums.TaskConditionResult;
import com.tc.dto.enums.TaskConditionSelect;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * 设置任务统计条件
 * @author Cyg
 */
public class TaskCondition {

    @Past
    private Timestamp begin;
    private Timestamp end;
    @NotNull
    private TaskConditionResult result;
    @NotNull
    private TaskConditionSelect select;
    @NotNull
    private List<String> items;



    public Timestamp getBegin() {
        return begin;
    }

    public void setBegin(Timestamp begin) {
        this.begin = begin;
    }

    public Timestamp getEnd() {
        return end;
    }

    public void setEnd(Timestamp end) {
        this.end = end;
    }

    public TaskConditionResult getResult() {
        return result;
    }

    public void setResult(TaskConditionResult result) {
        this.result = result;
    }

    public TaskConditionSelect getSelect() {
        return select;
    }

    public void setSelect(TaskConditionSelect select) {
        this.select = select;
    }

    public List<String> getItems() {
        return items;
    }

    public void setItems(List<String> items) {
        this.items = items;
    }

    public static List<Long> toIds(List<String> items) {
        List<Long> result = new ArrayList<>();
        for (String item : items) {
            try{
                result.add(Long.parseLong(item));
            }catch (Exception ignored){
            }
        }
        return result;
    }
}
