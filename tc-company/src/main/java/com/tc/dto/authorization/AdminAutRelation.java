package com.tc.dto.authorization;

import com.tc.db.entity.AdminAuthority;
import com.tc.db.entity.AuthorityResource;
import com.tc.dto.Show;

import java.io.Serializable;
import java.util.List;

/**
 * @author Cyg
 */
public class AdminAutRelation implements Serializable {
    private List<Show> admins;
    private List<Show> authorities;
    private List<AdminAuthority> adminAuthorities;

    public AdminAutRelation() {
    }

    public AdminAutRelation(List<Show> admins, List<Show> authorities, List<AdminAuthority> adminAuthorities) {
        this.admins = admins;
        this.authorities = authorities;
        this.adminAuthorities = adminAuthorities;
    }

    public List<Show> getAdmins() {
        return admins;
    }

    public void setAdmins(List<Show> admins) {
        this.admins = admins;
    }

    public List<Show> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<Show> authorities) {
        this.authorities = authorities;
    }

    public List<AdminAuthority> getAdminAuthorities() {
        return adminAuthorities;
    }

    public void setAdminAuthorities(List<AdminAuthority> adminAuthorities) {
        this.adminAuthorities = adminAuthorities;
    }

    public static AdminAutRelation init(List<Show> admins, List<Show> authorities, List<AdminAuthority> adminAuthorities){
        return new AdminAutRelation(admins,authorities,adminAuthorities);
    }
}
