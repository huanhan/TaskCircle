package com.tc.until;

import org.springframework.data.domain.Pageable;

import java.io.Serializable;

public abstract class AbstractPageRequest implements Pageable, Serializable {
    private static final long serialVersionUID = 1232825578694716871L;
    private int page;
    private int size;

    public AbstractPageRequest(int page, int size) {
        if (page < 0) {
            throw new IllegalArgumentException("Page index must not be less than zero!");
        } else if (size < 1) {
            throw new IllegalArgumentException("Page size must not be less than one!");
        } else {
            this.page = page;
            this.size = size;
        }
    }

    @Override
    public int getPageSize() {
        return this.size;
    }

    @Override
    public int getPageNumber() {
        return this.page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public int getOffset() {
        return this.page * this.size;
    }

    @Override
    public boolean hasPrevious() {
        return this.page > 0;
    }

    @Override
    public Pageable previousOrFirst() {
        return this.hasPrevious() ? this.previous() : this.first();
    }

    @Override
    public abstract Pageable next();

    public abstract Pageable previous();

    @Override
    public abstract Pageable first();

    @Override
    public int hashCode() {
        int prime = 1;
        int result = 1;
        result = 31 * result + this.page;
        result = 31 * result + this.size;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj != null && this.getClass() == obj.getClass()) {
            AbstractPageRequest other = (AbstractPageRequest)obj;
            return this.page == other.page && this.size == other.size;
        } else {
            return false;
        }
    }
}