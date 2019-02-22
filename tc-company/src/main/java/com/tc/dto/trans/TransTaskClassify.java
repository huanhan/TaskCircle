package com.tc.dto.trans;

import com.tc.db.entity.TaskClassify;

import java.util.List;

public class TransTaskClassify {
    List<TaskClassify> parents;
    List<TaskClassify> childrens;
    List<TransID> trans;




    public List<TaskClassify> getParents() {
        return parents;
    }

    public void setParents(List<TaskClassify> parents) {
        this.parents = parents;
    }

    public List<TaskClassify> getChildrens() {
        return childrens;
    }

    public void setChildrens(List<TaskClassify> childrens) {
        this.childrens = childrens;
    }

    public List<TransID> getTrans() {
        return trans;
    }

    public void setTrans(List<TransID> trans) {
        this.trans = trans;
    }
}
