package com.tc.dto;

import java.io.Serializable;
import java.util.List;

public class Result implements Serializable {
    private Object detail;
    private Object query;


    public Result() {
    }

    public Result(Object detail, Object query) {
        this.detail = detail;
        this.query = query;
    }


    public Object getDetail() {
        return detail;
    }

    public void setDetail(Object detail) {
        this.detail = detail;
    }

    public Object getQuery() {
        return query;
    }

    public void setQuery(Object query) {
        this.query = query;
    }

    public static Result init(Object detail, Object query){
        return new Result(detail,query);
    }

    public static Result init(Object detail){
        return new Result(detail,null);
    }

}
