package com.tc.dto.resource;

import com.tc.dto.MyPage;
import com.tc.until.PageRequest;
import org.springframework.data.domain.Sort;

import java.sql.Timestamp;

/**
 * 查询资源
 * @author Cyg
 */
public class QueryResource extends PageRequest {

    public static final String NAME = "name";
    public static final String INFO = "info";
    public static final String BEGIN = "begin";
    public static final String END = "end";
    public static final String CREATE_TIME = "createTime";
    public static final String METHOD = "method";
    public static final String CLASSNAME = "className";
    public static final String TYPE = "type";


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


    public QueryResource() {
        super(0,10);
    }

    public QueryResource(int page, int size) {
        super(page, size);
    }

    public QueryResource(int page, int size, Sort.Direction direction, String... properties) {
        super(page, size, direction, properties);
    }

    public QueryResource(int page, int size, Sort sort) {
        super(page, size, sort);
    }

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
