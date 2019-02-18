package com.tc.until;

import com.tc.db.entity.UserAuthority;
import com.tc.db.enums.UserCategory;

import java.util.ArrayList;
import java.util.List;

public class AUserOPClassify {
    private List<UserAuthority> inDefault;
    private List<UserAuthority> inDelete;
    private List<UserAuthority> inAdd;
    private List<Long> inAddId;

    public AUserOPClassify(List<UserAuthority> inDefault, List<UserAuthority> inDelete, List<UserAuthority> inAdd) {
        this.inDefault = inDefault;
        this.inDelete = inDelete;
        this.inAdd = inAdd;
        this.inAddId = new ArrayList<>();
        inAdd.forEach(userAuthority -> inAddId.add(userAuthority.getAuthorityId()));
    }

    public List<UserAuthority> getInDefault() {
        return inDefault;
    }

    public List<UserAuthority> getInDelete() {
        return inDelete;
    }

    public List<UserAuthority> getInAdd() {
        return inAdd;
    }

    public List<Long> getInAddId() {
        return inAddId;
    }

    /**
     * 获取操作之后最新的数据
     * @return
     */
    public List<UserAuthority> getBestNews() {
        List<UserAuthority> def = new ArrayList<>();
        if (ListUtils.isNotEmpty(getInDefault())){
            def.addAll(getInDefault());
        }
        if (ListUtils.isNotEmpty(getInAdd())){
            def.addAll(getInAdd());
        }
        return def;
    }

    public static AUserOPClassify init(List<UserAuthority> news, List<UserAuthority> oldies){
        List<UserAuthority> inDefault = new ArrayList<>();
        List<UserAuthority> inDelete = new ArrayList<>();
        List<UserAuthority> inAdd = new ArrayList<>();

        if (!news.isEmpty() && !oldies.isEmpty()){
            List<UserAuthority> middle = new ArrayList<>();
            //取交集
            news.forEach(nar -> oldies.forEach(oar ->{
                if (nar.getCategory().equals(oar.getCategory()) && nar.getAuthorityId().equals(oar.getAuthorityId())){
                    middle.add(nar);
                }
            }));
            if (!middle.isEmpty()){
                //存在交集
                //新和旧的分别移出交集
                for (UserAuthority mar:
                     middle) {
                    news.removeIf(userAuthority ->
                            userAuthority.getAuthorityId().equals(mar.getAuthorityId())
                            && userAuthority.getCategory().equals(mar.getCategory())
                    );
                    oldies.removeIf(userAuthority ->
                            userAuthority.getAuthorityId().equals(mar.getAuthorityId())
                            && userAuthority.getCategory().equals(mar.getCategory())
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

        return new AUserOPClassify(inDefault,inDelete,inAdd);

    }


    public static List<UserAuthority> create(UserCategory aid, List<Long> rids){
        List<UserAuthority> userAuthorities = new ArrayList<>();
        if (aid != null && !ListUtils.isEmpty(rids)) {
            rids.forEach(aLong -> userAuthorities.add(new UserAuthority(aLong, aid)));
        }
        return userAuthorities;
    }

    public static List<Long> toAuthorityLong(List<UserAuthority> userAuthorities){
        List<Long> rids = new ArrayList<>();
        if (!ListUtils.isEmpty(userAuthorities)) {
            userAuthorities.forEach(userAuthority -> rids.add(userAuthority.getAuthorityId()));
        }
        return rids;
    }

}
