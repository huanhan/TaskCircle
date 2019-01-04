package com.tc.dto;

/**
 * @author Cyg
 *
 * {id:name}的记录信息
 */
public class Show {
    private Long id;
    private String sid;
    private String name;
    private Long queryId;

    public Show() {
    }

    public Show(String sid, String name) {
        this.sid = sid;
        this.name = name;
    }

    public Show(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Show(Long id, String name,Long queryId) {
        this.id = id;
        this.name = name;
        this.queryId = queryId;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
}
