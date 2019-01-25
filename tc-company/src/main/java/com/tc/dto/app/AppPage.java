package com.tc.dto.app;

import org.springframework.data.domain.Page;

import java.util.List;

public class AppPage {
    private List content;
    private long totalNum;
    private int totalPage;
    private int pageNum;
    private int pageSize;

    public AppPage(List content, long totalNum, int totalPage, int pageNum, int pageSize) {
        this.content = content;
        this.totalNum = totalNum;
        this.totalPage = totalPage;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }

    public List getContent() {
        return content;
    }

    public void setContent(List content) {
        this.content = content;
    }

    public long getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(long totalNum) {
        this.totalNum = totalNum;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public static AppPage init(List list, Page page) {
        return new AppPage(list,// 数据集合
                page.getTotalElements(),// 总记录数
                page.getTotalPages(),// 总页数
                page.getNumber(),// 当前页码
                page.getSize());// 每页显示数量
    }
}
