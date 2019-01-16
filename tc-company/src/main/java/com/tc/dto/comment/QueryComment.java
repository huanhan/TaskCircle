package com.tc.dto.comment;

import com.tc.until.PageRequest;
import org.springframework.data.domain.Sort;

public class QueryComment extends PageRequest {
    public QueryComment(int page, int size) {
        super(page, size);
    }

    public QueryComment(int page, int size, Sort.Direction direction, String... properties) {
        super(page, size, direction, properties);
    }

    public QueryComment(int page, int size, Sort sort) {
        super(page, size, sort);
    }
}
