package com.tc.db.entity.pk;

import com.tc.db.enums.UserCategory;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class UserAuthorityPK implements Serializable {
    private Long authorityId;
    private UserCategory category;

    @Column(name = "authority_id")
    @Id
    public Long getAuthorityId() {
        return authorityId;
    }

    public void setAuthorityId(Long authorityId) {
        this.authorityId = authorityId;
    }

    @Column(name = "category")
    @Enumerated(EnumType.STRING)
    @Id
    public UserCategory getCategory() {
        return category;
    }

    public void setCategory(UserCategory category) {
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
        UserAuthorityPK that = (UserAuthorityPK) o;
        return authorityId == that.authorityId &&
                Objects.equals(category, that.category);
    }

    @Override
    public int hashCode() {

        return Objects.hash(authorityId, category);
    }
}
