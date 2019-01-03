package com.tc.dto;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * 我的分页
 * @author Cyg
 */
public class MyPage {
    private Integer pageIndex = 0;
    private Integer pageSize = 10;
    private Sort sort;

    public MyPage() {
    }

    public MyPage(Integer pageIndex, Integer pageSize) {
        this(pageIndex,pageSize,null);
    }

    public MyPage(Integer pageIndex, Integer pageSize, Sort sort) {
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
        this.sort = sort;
    }

    public MyPage(int page, int size, Sort.Direction direction, String... properties) {
        this(page, size, new Sort(direction, properties));
    }

    public Integer getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Sort getSort() {
        return sort;
    }

    public void setSort(Sort sort) {
        this.sort = sort;
    }

    public Pageable toPageRequest(){
        Pageable pageable = new PageRequest(pageIndex,pageSize,sort);
        return pageable;
    }
}
