package com.tc.dto.user;

import com.tc.db.entity.AdminAuthority;
import com.tc.db.entity.Authority;
import com.tc.db.entity.User;
import com.tc.db.entity.UserAuthority;
import com.tc.db.enums.UserCategory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.social.security.SocialUserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class LoginUser implements UserDetails ,SocialUserDetails {

    private User user;

    public LoginUser(User user) {
        this.user = user;
    }

    @Override
    public Collection<Authority> getAuthorities() {

        List<Authority> authorities = new ArrayList<>();

        if (user.getCategory().equals(UserCategory.ADMINISTRATOR)){
            for (AdminAuthority adminAuthority :
                    user.getAdmin().getAdminAuthorities()) {
                authorities.add(adminAuthority.getAuthority());
            }
        }else {
            for(UserAuthority userAuthority : user.getUserAuthorities()){
                authorities.add(userAuthority.getAuthority());
            }
        }

        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getUserId() {
        return user.getId().toString();
    }
}
