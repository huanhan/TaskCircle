package com.tc.dto.user;

import com.tc.db.entity.Authority;
import com.tc.db.entity.User;
import com.tc.db.entity.UserAuthority;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.social.security.SocialUserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class LoginUser implements UserDetails,SocialUserDetails {

    private User user;

    public LoginUser(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        List<SimpleGrantedAuthority> simpleAuthorities = new ArrayList<>();
//        for(UserAuthority userAuthority : user.getAuthorities()){
//            Authority authority = userAuthority.getAuthority();
//            simpleAuthorities.add(new SimpleGrantedAuthority(authority.getAuthority()));
//        }
        simpleAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN,ROLE_USER"));
        return simpleAuthorities;
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
