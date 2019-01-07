package com.tc.until;

import java.util.Collection;
import java.util.List;

public class ListUtils extends org.apache.commons.collections.ListUtils {

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
