package com.tc.until;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * 列表帮助类
 * @author Cyg
 */
public class ListHelper<T> {
    public Page<T> paging(List<T> list, int pageNumber, int pageSize){
        int currIdx = (pageNumber > 1 ? (pageNumber -1) * pageSize : 0);
        List<T> result = new ArrayList<>();
        for (int i = 0; i < pageSize && i < list.size() - currIdx; i++) {
            T t = list.get(currIdx + i);
            result.add(t);
        }
        return new PageImpl<T>(result,new PageRequest(pageNumber,pageSize),list.size());
    }
}
