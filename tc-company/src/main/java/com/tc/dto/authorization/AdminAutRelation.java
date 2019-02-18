package com.tc.dto.authorization;

import com.tc.db.entity.AdminAuthority;
import com.tc.db.entity.AuthorityResource;
import com.tc.dto.Show;
import com.tc.dto.trans.Trans;
import com.tc.dto.trans.TransID;
import com.tc.dto.trans.TransOP;

import java.io.Serializable;
import java.util.List;

/**
 * @author Cyg
 */
public class AdminAutRelation implements Serializable {
    private List<Trans> admins;
    private List<TransOP> authorities;
    private List<TransID> adminAuthorities;

    public AdminAutRelation() {
    }

    public AdminAutRelation(List<Trans> admins, List<TransOP> authorities, List<TransID> adminAuthorities) {
        this.admins = admins;
        this.authorities = authorities;
        this.adminAuthorities = adminAuthorities;
    }

    public List<Trans> getAdmins() {
        return admins;
    }

    public void setAdmins(List<Trans> admins) {
        this.admins = admins;
    }

    public List<TransOP> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<TransOP> authorities) {
        this.authorities = authorities;
    }

    public List<TransID> getAdminAuthorities() {
        return adminAuthorities;
    }

    public void setAdminAuthorities(List<TransID> adminAuthorities) {
        this.adminAuthorities = adminAuthorities;
    }

    public static AdminAutRelation init(List<Trans> admins, List<TransOP> authorities, List<TransID> adminAuthorities){
        return new AdminAutRelation(admins,authorities,adminAuthorities);
    }
}
