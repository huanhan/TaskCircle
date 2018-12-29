package com.tc.dto.authorization;

import com.tc.db.entity.AuthorityResource;
import com.tc.dto.Show;

import java.io.Serializable;
import java.util.List;

/**
 * @author Cyg
 */
public class UserAutRelation implements Serializable {
    private List<Show> resources;
    private List<Show> authorities;
    private List<AuthorityResource> autResIds;

    public UserAutRelation() {
    }

    public UserAutRelation(List<Show> resources, List<Show> authorities, List<AuthorityResource> autResIds) {
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

    public static UserAutRelation init(List<Show> resources, List<Show> authorities, List<AuthorityResource> autResIds){
        return new UserAutRelation(resources,authorities,autResIds);
    }
}
