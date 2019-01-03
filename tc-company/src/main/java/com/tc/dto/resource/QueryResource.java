package com.tc.dto.resource;

import com.tc.dto.MyPage;

import java.sql.Timestamp;

/**
 * 查询资源
 * @author Cyg
 */
public class QueryResource extends MyPage {

    /**
     * 资源名
     */
    private String name;

    /**
     * 资源信息，模糊查询
     */
    private String info;

    /**
     * 开始时间
     */
    private Timestamp begin;

    /**
     * 结束时间
     */
    private Timestamp end;

    /**
     * 请求方法
     */
    private String method;

    /**
     * 请求类
     */
    private String className;

    /**
     * 请求类型
     */
    private String type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

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

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
