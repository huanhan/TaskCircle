package com.tc.validator.until;

import com.tc.db.entity.Authority;
import com.tc.db.entity.AuthorityResource;
import com.tc.db.entity.Resource;

import java.util.ArrayList;
import java.util.List;

public class AuthorityOPClassify {
    List<AuthorityResource> inDefault;
    List<AuthorityResource> inDelete;
    List<AuthorityResource> inAdd;
    List<Long> inAddId;

    public AuthorityOPClassify(List<AuthorityResource> inDefault, List<AuthorityResource> inDelete, List<AuthorityResource> inAdd) {
        this.inDefault = inDefault;
        this.inDelete = inDelete;
        this.inAdd = inAdd;

        this.inAddId = new ArrayList<>();
        inAdd.forEach(authorityResource -> inAddId.add(authorityResource.getResource().getId()));
    }

    public List<AuthorityResource> getInDefault() {
        return inDefault;
    }

    public List<AuthorityResource> getInDelete() {
        return inDelete;
    }

    public List<AuthorityResource> getInAdd() {
        return inAdd;
    }

    public List<Long> getInAddId() {
        return inAddId;
    }

    public static AuthorityOPClassify init(List<AuthorityResource> news, List<AuthorityResource> olds){
        List<AuthorityResource> inDefault = null;
        List<AuthorityResource> inDelete = null;
        List<AuthorityResource> inAdd = null;

        if (!news.isEmpty() && !olds.isEmpty()){
            List<AuthorityResource> middle = new ArrayList<>(news);
            boolean hasRetain = middle.retainAll(olds);
            if (hasRetain){
                news.removeAll(middle);
                olds.removeAll(middle);
                inDefault = new ArrayList<>(middle);
            }else {
                inDefault = new ArrayList<>();
            }
            inDelete = new ArrayList<>(olds);
            inAdd = new ArrayList<>(news);
        } else if (news.isEmpty() && !olds.isEmpty()){
            inDefault = new ArrayList<>();
            inDelete = new ArrayList<>(olds);
            inAdd = new ArrayList<>();
        } else if (!news.isEmpty()){
            inDefault = new ArrayList<>();
            inDelete = new ArrayList<>();
            inAdd = new ArrayList<>(news);
        }

        return new AuthorityOPClassify(inDefault,inDelete,inAdd);

    }

    public static List<AuthorityResource> create(Long aid,List<Long> rids){
        List<AuthorityResource> authorityResources = new ArrayList<>();
        rids.forEach(aLong -> authorityResources.add(new AuthorityResource(aid,aLong)));
        return authorityResources;
    }

    public static List<Long> toResourceLong(List<AuthorityResource> authorityResources){
        List<Long> rids = new ArrayList<>();
        authorityResources.forEach(authorityResource -> rids.add(authorityResource.getResourceId()));
        return rids;
    }

    public static List<AuthorityResource> toAuthorityResource(Authority authority, List<Resource> resources){
        List<AuthorityResource> authorityResources = new ArrayList<>();
        resources.forEach(resource -> authorityResources.add(new AuthorityResource(authority,resource)));
        return authorityResources;
    }
}
