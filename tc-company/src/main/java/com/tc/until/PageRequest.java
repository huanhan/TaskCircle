package com.tc.until;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.Serializable;


/**
 * 分页与排序
 * @author Cyg
 */
public class PageRequest extends AbstractPageRequest {

    private static final long serialVersionUID = -4541509938956089562L;
    private Sort sort;
    private Page pageDto;

    public PageRequest() {
        super(0,10);
    }

    public PageRequest(int page, int size) {
        this(page, size, (Sort)null);
    }

    public PageRequest(int page, int size, Sort.Direction direction, String... properties) {
        this(page, size, new Sort(direction, properties));
    }

    public PageRequest(int page, int size, Sort sort) {
        super(page, size);
        this.sort = sort;
    }

    @Override
    public Sort getSort() {
        return this.sort;
    }

    public void setSort(Sort sort) {
        this.sort = sort;
    }

    public Page getPageDto() {
        return pageDto;
    }

    public void setPageDto(Page pageDto) {
        this.pageDto = pageDto;
    }

    @Override
    public Pageable next() {
        return new org.springframework.data.domain.PageRequest(this.getPageNumber() + 1, this.getPageSize(), this.getSort());
    }

    @Override
    public PageRequest previous() {
        return this.getPageNumber() == 0 ? this : new PageRequest(this.getPageNumber() - 1, this.getPageSize(), this.getSort());
    }

    @Override
    public Pageable first() {
        return new org.springframework.data.domain.PageRequest(0, this.getPageSize(), this.getSort());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (!(obj instanceof PageRequest)) {
            return false;
        } else {
            PageRequest that = (PageRequest)obj;
            boolean sortEqual = this.sort == null ? that.sort == null : this.sort.equals(that.sort);
            return super.equals(that) && sortEqual;
        }
    }

    @Override
    public int hashCode() {
        return 31 * super.hashCode() + (null == this.sort ? 0 : this.sort.hashCode());
    }

    @Override
    public String toString() {
        return String.format("Page request [number: %d, size %d, sort: %s]", this.getPageNumber(), this.getPageSize(), this.sort == null ? null : this.sort.toString());
    }

    public PageRequest append(Page page){
        this.pageDto = page;
        return this;
    }

    public PageRequest append(Long count,Long pages){
        this.pageDto = new Page(count,pages,(long) this.getPageNumber(),(long) this.getPageSize());
        return this;
    }
}
