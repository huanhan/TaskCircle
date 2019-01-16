package com.tc.dto.finance;

import com.tc.until.PageRequest;
import org.springframework.data.domain.Sort;

/**
 * 查询用户转账信息
 * @author Cyg
 */
public class QueryIE extends PageRequest {
    public QueryIE(int page, int size) {
        super(page, size);
    }

    public QueryIE(int page, int size, Sort.Direction direction, String... properties) {
        super(page, size, direction, properties);
    }

    public QueryIE(int page, int size, Sort sort) {
        super(page, size, sort);
    }
}
