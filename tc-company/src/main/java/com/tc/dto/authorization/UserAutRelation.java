package com.tc.dto.authorization;

import com.tc.db.entity.UserAuthority;
import com.tc.dto.Show;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Cyg
 */
public class UserAutRelation implements Serializable {
    private List<Show> userCategories;
    private List<Show> authorities;
    private List<UserAuthority> userAuthorities;

    public UserAutRelation() {
    }

    public UserAutRelation(List<Show> userCategories, List<Show> authorities, List<UserAuthority> userAutIds) {
        this.userCategories = userCategories;
        this.authorities = authorities;
        this.userAuthorities = userAutIds;
    }

    public List<Show> getUserCategories() {
        return userCategories;
    }

    public void setUserCategories(List<Show> userCategories) {
        this.userCategories = userCategories;
    }

    public List<Show> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<Show> authorities) {
        this.authorities = authorities;
    }

    public List<UserAuthority> getUserAuthorities() {
        return userAuthorities;
    }

    public void setUserAuthorities(List<UserAuthority> userAuthorities) {
        this.userAuthorities = userAuthorities;
    }

    public static UserAutRelation init(){
        return new UserAutRelation(new ArrayList<>(),new ArrayList<>(),new ArrayList<>());
    }

    public static UserAutRelation init(List<Show> userCategories, List<Show> authorities, List<UserAuthority> userAutIds){
        return new UserAutRelation(userCategories,authorities,userAutIds);
    }
}
