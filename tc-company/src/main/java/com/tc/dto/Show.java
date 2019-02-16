package com.tc.dto;

import java.util.List;

/**
 * @author Cyg
 *
 * {id:name}的记录信息
 */
public class Show {
    private Object id;
    private String name;
    private Long queryId;
    private List<Show> children;
    private List<TransEnum> transEnums;


    public Show() {
    }

    public Show(Object id, String name) {
        this.id = id;
        this.name = name;
    }

    public Show(Object id, String name, List<Show> children) {
        this.id = id;
        this.name = name;
        this.children = children;
    }

    public Show(Object id, String name, Long queryId) {
        this.id = id;
        this.name = name;
        this.queryId = queryId;
    }

    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getQueryId() {
        return queryId;
    }

    public void setQueryId(Long queryId) {
        this.queryId = queryId;
    }

    public List<Show> getChildren() {
        return children;
    }

    public void setChildren(List<Show> children) {
        this.children = children;
    }

    public List<TransEnum> getTransEnums() {
        return transEnums;
    }

    public void setTransEnums(List<TransEnum> transEnums) {
        this.transEnums = transEnums;
    }
}
