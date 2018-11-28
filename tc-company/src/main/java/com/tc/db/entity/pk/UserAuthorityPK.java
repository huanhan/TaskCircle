package com.tc.db.entity.pk;

import com.tc.db.entity.Authority;
import com.tc.db.entity.User;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class UserAuthorityPK implements Serializable {
    private Authority authority;
    private User user;

    @Column(name = "authority_id")
    @Id
    public Authority getAuthority() {
        return authority;
    }

    public void setAuthority(Authority authorityByAuthorityId) {
        this.authority = authorityByAuthorityId;
    }

    @Column(name = "user_id")
    @Id
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {return true;}
        if (o == null || getClass() != o.getClass()) {return false;}
        UserAuthorityPK that = (UserAuthorityPK) o;
        return authority.getId().equals(that.getAuthority().getId()) &&
                user.getId().equals(that.getUser().getId());
    }

    @Override
    public int hashCode() {

        return Objects.hash(authority.getId(), user.getId());
    }
}
