package com.tc.until;


/**
 * 分页信息
 * @author Cyg
 */
public class Page {
    private Long count;
    private Long pages;
    private Long currentPage;
    private Long pageSize;

    public Page() {
    }

    public Page(Long count, Long pages, Long currentPage, Long pageSize) {
        this.count = count;
        this.pages = pages;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public Long getPages() {
        return pages;
    }

    public void setPages(Long pages) {
        this.pages = pages;
    }

    public Long getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Long currentPage) {
        this.currentPage = currentPage;
    }

    public Long getPageSize() {
        return pageSize;
    }

    public void setPageSize(Long pageSize) {
        this.pageSize = pageSize;
    }
}
