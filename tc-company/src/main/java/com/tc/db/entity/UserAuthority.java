package com.tc.db.entity;

import com.tc.db.entity.pk.UserAuthorityPK;
import com.tc.db.enums.UserCategory;
import com.tc.dto.Show;
import com.tc.dto.trans.TransEnum;
import com.tc.dto.trans.Trans;
import com.tc.until.ListUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 用户与权限关系
 * @author Cyg
 */
@Entity
@Table(name = "user_authority", schema = "tc-company")
@IdClass(UserAuthorityPK.class)
public class UserAuthority {

    public static final String CATEGORY = "category";

    private Long authorityId;
    private UserCategory category;
    private Authority authority;

    public UserAuthority() {
    }

    public UserAuthority(Long authorityId, UserCategory category) {
        this.authorityId = authorityId;
        this.category = category;
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
    @Column(name = "category")
    @Enumerated(EnumType.STRING)
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
        UserAuthority that = (UserAuthority) o;
        return authorityId.equals(that.authorityId) &&
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

    public static List<Show> toShows(List<UserAuthority> list){
        List<Show> result = new ArrayList<>();
        if (!list.isEmpty()){
            list.forEach(userAuthority -> {
                result.add(new Show(userAuthority.getCategory().name(),userAuthority.getCategory().getCategory()));
            });
        }
        return result;
    }

    public static Show toShow(List<UserAuthority> list, List<TransEnum> categories, Authority query) {
        Show result = new Show();
        List<Show> children = new ArrayList<>();
        if (!list.isEmpty()){
            list.forEach(userAuthority ->
                    children.add(new Show(userAuthority.getCategory().name(),userAuthority.getCategory().getCategory())));
        }
        result.setChildren(children);
        result.setTransEnums(categories);
        result.setId(query.getId());
        result.setName(query.getName());
        return result;
    }

    public static List<UserAuthority> reset(List<UserAuthority> userAuthorities){
        if (!userAuthorities.isEmpty()){
            userAuthorities.forEach(userAuthority -> {
                userAuthority.setAuthority(null);
            });
        }
        return userAuthorities;
    }

    public static List<UserAuthority> by(Long id, List<UserCategory> ids) {
        List<UserAuthority> result = new ArrayList<>();
        if (ListUtils.isNotEmpty(ids)){
            ids.forEach(userCategory -> result.add(new UserAuthority(id,userCategory)));
        }
        return result;
    }


    public static List<Trans> toTrans(List<UserAuthority> list) {
        List<Trans> result = new ArrayList<>();
        if (ListUtils.isNotEmpty(list)){
            list.forEach(userAuthority -> result.add(new Trans(userAuthority.authorityId,userAuthority.category.name())));
        }
        return result;
    }
}
