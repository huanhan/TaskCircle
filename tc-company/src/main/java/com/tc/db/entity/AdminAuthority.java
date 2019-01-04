package com.tc.db.entity;

import com.tc.db.entity.pk.AdminAuthorityPK;

import javax.persistence.*;
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

    private Long authorityId;
    private Long userId;
    private Authority authority;
    private Admin admin;

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
}
