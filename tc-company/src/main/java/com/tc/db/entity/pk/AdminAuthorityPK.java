package com.tc.db.entity.pk;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class AdminAuthorityPK implements Serializable {
    private long authorityId;
    private long userId;

    @Column(name = "authority_id")
    @Id
    public long getAuthorityId() {
        return authorityId;
    }

    public void setAuthorityId(long authorityId) {
        this.authorityId = authorityId;
    }

    @Column(name = "user_id")
    @Id
    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AdminAuthorityPK that = (AdminAuthorityPK) o;
        return authorityId == that.authorityId &&
                userId == that.userId;
    }

    @Override
    public int hashCode() {

        return Objects.hash(authorityId, userId);
    }
}
