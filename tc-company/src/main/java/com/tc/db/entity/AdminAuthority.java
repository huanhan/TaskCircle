package com.tc.db.entity;

import com.tc.db.entity.pk.AdminAuthorityPK;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "admin_authority", schema = "tc-company", catalog = "")
@IdClass(AdminAuthorityPK.class)
public class AdminAuthority {
    private long authorityId;
    private long userId;
    private Authority authority;
    private Admin admin;

    @Id
    @Column(name = "authority_id")
    public long getAuthorityId() {
        return authorityId;
    }

    public void setAuthorityId(long authorityId) {
        this.authorityId = authorityId;
    }

    @Id
    @Column(name = "user_id")
    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AdminAuthority that = (AdminAuthority) o;
        return authorityId == that.authorityId &&
                userId == that.userId;
    }

    @Override
    public int hashCode() {

        return Objects.hash(authorityId, userId);
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
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false,insertable = false,updatable = false)
    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }
}
