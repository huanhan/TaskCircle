package com.tc.dto;

import com.tc.db.entity.AuthorityResource;

import java.io.Serializable;
import java.util.List;

public class AutResRelation implements Serializable {
    private List<Show> resources;
    private List<Show> authorities;
    private List<AuthorityResource> autResIds;

    public AutResRelation() {
    }

    public AutResRelation(List<Show> resources, List<Show> authorities, List<AuthorityResource> autResIds) {
        this.resources = resources;
        this.authorities = authorities;
        this.autResIds = autResIds;
    }

    public List<Show> getResources() {
        return resources;
    }

    public void setResources(List<Show> resources) {
        this.resources = resources;
    }

    public List<Show> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<Show> authorities) {
        this.authorities = authorities;
    }

    public List<AuthorityResource> getAutResIds() {
        return autResIds;
    }

    public void setAutResIds(List<AuthorityResource> autResIds) {
        this.autResIds = autResIds;
    }

    public static AutResRelation init(List<Show> resources, List<Show> authorities, List<AuthorityResource> autResIds){
        return new AutResRelation(resources,authorities,autResIds);
    }
}
