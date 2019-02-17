package com.tc.dto.authorization;

import com.tc.dto.trans.Trans;
import com.tc.dto.trans.TransOP;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Cyg
 */
public class UserAutRelation implements Serializable {
    private List<Trans> userCategories;
    private List<TransOP> authorities;
    private List<Trans> userAuthorities;

    public UserAutRelation() {
    }

    public UserAutRelation(List<Trans> userCategories, List<TransOP> authorities, List<Trans> userAutIds) {
        this.userCategories = userCategories;
        this.authorities = authorities;
        this.userAuthorities = userAutIds;
    }

    public List<Trans> getUserCategories() {
        return userCategories;
    }

    public void setUserCategories(List<Trans> userCategories) {
        this.userCategories = userCategories;
    }

    public List<TransOP> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<TransOP> authorities) {
        this.authorities = authorities;
    }

    public List<Trans> getUserAuthorities() {
        return userAuthorities;
    }

    public void setUserAuthorities(List<Trans> userAuthorities) {
        this.userAuthorities = userAuthorities;
    }

    public static UserAutRelation init(){
        return new UserAutRelation(new ArrayList<>(),new ArrayList<>(),new ArrayList<>());
    }

    public static UserAutRelation init(List<Trans> userCategories, List<TransOP> authorities, List<Trans> userAutIds){
        return new UserAutRelation(userCategories,authorities,userAutIds);
    }
}
