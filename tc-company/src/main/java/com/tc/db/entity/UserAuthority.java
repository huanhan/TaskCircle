package com.tc.db.entity;

import com.tc.db.entity.pk.UserAuthorityPK;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "user_authority", schema = "tc-company")
@IdClass(UserAuthorityPK.class)
public class UserAuthority {
    private long authorityId;
    private String category;
    private Authority authority;

    @Id
    @Column(name = "authority_id")
    public long getAuthorityId() {
        return authorityId;
    }

    public void setAuthorityId(long authorityId) {
        this.authorityId = authorityId;
    }

    @Id
    @Column(name = "category")
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserAuthority that = (UserAuthority) o;
        return authorityId == that.authorityId &&
                Objects.equals(category, that.category);
    }

    @Override
    public int hashCode() {

        return Objects.hash(authorityId, category);
    }

    @ManyToOne
    @JoinColumn(name = "authority_id", referencedColumnName = "id", nullable = false,insertable = false,updatable = false)
    public Authority getAuthority() {
        return authority;
    }

    public void setAuthority(Authority authority) {
        this.authority = authority;
    }
}
