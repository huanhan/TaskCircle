package com.tc.db.entity;

import com.tc.db.entity.pk.AdminAuthorityPK;
import com.tc.db.enums.UserCategory;
import com.tc.dto.Show;
import com.tc.dto.trans.Trans;
import com.tc.until.ListUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 管理员与权限关系
 * @author Cyg
 */
@Entity
@Table(name = "admin_authority", schema = "tc-company")
@IdClass(AdminAuthorityPK.class)
public class AdminAuthority {

    public static final String AUTHORITY_ID = "authorityId";
    public static final String USER_ID = "userId";
    public static final String ADMIN = "admin";

    private Long authorityId;
    private Long userId;
    private Authority authority;
    private Admin admin;

    public AdminAuthority() {
    }

    public AdminAuthority(Long authorityId, Long userId) {
        this.authorityId = authorityId;
        this.userId = userId;
    }




    @Id
    @Column(name = "authority_id")
    public Long getAuthorityId() {
        return authorityId;
    }

    public void setAuthorityId(Long authorityId) {
        this.authorityId = authorityId;
    }

    @Id
    @Column(name = "user_id")
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }



    @ManyToOne
    @JoinColumn(name = "authority_id", referencedColumnName = "id", nullable = false,insertable = false,updatable = false)
    public Authority getAuthority() {
        return authority;
    }

    public void setAuthority(Authority authority) {
        this.authority = authority;
    }

    @ManyToOne
    @JoinColumns(@JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false, insertable = false, updatable = false))
    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AdminAuthority that = (AdminAuthority) o;
        return authorityId.equals(that.authorityId) &&
                userId.equals(that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authorityId, userId);
    }

    public static List<AdminAuthority> reset(List<AdminAuthority> list) {
        if (!list.isEmpty()){
            list.forEach(authorityResource -> {
                    authorityResource.setAuthority(null);
                    authorityResource.setAdmin(null);
                }
            );
        }
        return list;
    }

    public static List<AdminAuthority> by(Long id, List<Long> ids) {
        List<AdminAuthority> result = new ArrayList<>();
        if (ListUtils.isNotEmpty(ids)){
            ids.forEach(aLong -> result.add(new AdminAuthority(id,aLong)));
        }
        return result;
    }

    public static List<Trans> toTrans(List<AdminAuthority> adminAuthorities) {
        List<Trans> result = new ArrayList<>();
        if (ListUtils.isNotEmpty(adminAuthorities)){
            adminAuthorities.forEach(adminAuthority -> {
                result.add(new Trans(adminAuthority.userId));
            });
        }
        return result;
    }
}
