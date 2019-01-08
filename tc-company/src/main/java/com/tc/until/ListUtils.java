package com.tc.until;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ListUtils extends org.apache.commons.collections.ListUtils {


    public static <T> PageImpl paging(List<T> list, int pageNumber, int pageSize){
        int currIdx = (pageNumber > 1 ? (pageNumber -1) * pageSize : 0);
        List<T> result = new ArrayList<>();
        for (int i = 0; i < pageSize && i < list.size() - currIdx; i++) {
            T t = list.get(currIdx + i);
            result.add(t);
        }
        return new PageImpl(result,new PageRequest(pageNumber,pageSize),list.size());
    }

    public static boolean isEmpty(List list){
        if (list == null) {
            return true;
        }
        return list.size() == 0;
    }

    public static boolean isEmpty(Collection list){
        if (list == null) {
            return true;
        }
        return list.size() == 0;
    }
}
