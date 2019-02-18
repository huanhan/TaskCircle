package com.tc.until;

import com.tc.db.entity.AdminAuthority;
import com.tc.db.entity.AdminAuthority;
import com.tc.db.enums.UserCategory;

import java.util.ArrayList;
import java.util.List;

public class AAdminOPClassify {
    List<AdminAuthority> inDefault;
    List<AdminAuthority> inDelete;
    List<AdminAuthority> inAdd;
    List<Long> inAddId;

    public AAdminOPClassify(List<AdminAuthority> inDefault, List<AdminAuthority> inDelete, List<AdminAuthority> inAdd) {
        this.inDefault = inDefault;
        this.inDelete = inDelete;
        this.inAdd = inAdd;
        this.inAddId = new ArrayList<>();
        inAdd.forEach(adminAuthority -> inAddId.add(adminAuthority.getAuthorityId()));
    }

    public List<AdminAuthority> getInDefault() {
        return inDefault;
    }

    public List<AdminAuthority> getInDelete() {
        return inDelete;
    }

    public List<AdminAuthority> getInAdd() {
        return inAdd;
    }

    public List<Long> getInAddId() {
        return inAddId;
    }

    public List<AdminAuthority> getBestNews() {
        List<AdminAuthority> def = new ArrayList<>();
        if (ListUtils.isNotEmpty(getInDefault())){
            def.addAll(getInDefault());
        }
        if (ListUtils.isNotEmpty(getInAdd())){
            def.addAll(getInAdd());
        }
        return def;
    }

    public static AAdminOPClassify init(List<AdminAuthority> news, List<AdminAuthority> oldies){
        List<AdminAuthority> inDefault = new ArrayList<>();
        List<AdminAuthority> inDelete = new ArrayList<>();
        List<AdminAuthority> inAdd = new ArrayList<>();

        if (!news.isEmpty() && !oldies.isEmpty()){
            List<AdminAuthority> middle = new ArrayList<>();
            //取交集
            news.forEach(nar -> oldies.forEach(oar ->{
                if (nar.getUserId().equals(oar.getUserId()) && nar.getAuthorityId().equals(oar.getAuthorityId())){
                    middle.add(nar);
                }
            }));
            if (!middle.isEmpty()){
                //存在交集
                //新和旧的分别移出交集
                for (AdminAuthority mar:
                     middle) {
                    news.removeIf(adminAuthority ->
                            adminAuthority.getAuthorityId().equals(mar.getAuthorityId())
                            && adminAuthority.getUserId().equals(mar.getUserId())
                    );
                    oldies.removeIf(adminAuthority ->
                            adminAuthority.getAuthorityId().equals(mar.getAuthorityId())
                            && adminAuthority.getUserId().equals(mar.getUserId())
                    );
                }
                inDefault = middle;
            }
            inDelete = oldies;
            inAdd = news;
        } else if (!oldies.isEmpty()){
            inDelete = oldies;
        } else if (!news.isEmpty()){
            inAdd = news;
        }
        return new AAdminOPClassify(inDefault,inDelete,inAdd);

    }

    public static List<AdminAuthority> create(Long aid, List<Long> rids){
        List<AdminAuthority> userAuthorities = new ArrayList<>();
        rids.forEach(rid -> userAuthorities.add(new AdminAuthority(rid,aid)));
        return userAuthorities;
    }

    public static List<Long> toAuthorityLong(List<AdminAuthority> adminAuthorities){
        List<Long> rids = new ArrayList<>();
        adminAuthorities.forEach(adminAuthority -> rids.add(adminAuthority.getAuthorityId()));
        return rids;
    }


}
