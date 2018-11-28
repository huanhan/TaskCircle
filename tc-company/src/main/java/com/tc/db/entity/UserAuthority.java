package com.tc.db.entity;

import com.tc.db.entity.pk.UserAuthorityPK;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * @author Cyg
 * 用户与权限关系实体
 */
@Entity
@Table(name = "user_authority", schema = "tc-company")
@IdClass(UserAuthorityPK.class)
public class UserAuthority implements Serializable {

    private Authority authority;
    private User user;

    @Override
    public boolean equals(Object o) {
        if (this == o) {return true;}
        if (o == null || getClass() != o.getClass()) {return false;}
        UserAuthority that = (UserAuthority) o;
        return authority.getId().equals(that.getAuthority().getId()) &&
                user.getId().equals(that.getUser().getId());
    }

    @Override
    public int hashCode() {

        return Objects.hash(authority.getId(), user.getId());
    }

    @Id
    @ManyToOne
    @JoinColumn(name = "authority_id", referencedColumnName = "id", nullable = false)
    public Authority getAuthority() {
        return authority;
    }

    public void setAuthority(Authority authorityByAuthorityId) {
        this.authority = authorityByAuthorityId;
    }

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
