package com.tc.service.impl;

import com.tc.db.entity.User;
import com.tc.db.repository.UserRepository;
import com.tc.dto.LoginUser;
import com.tc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.social.security.SocialUserDetails;
import org.springframework.social.security.SocialUserDetailsService;
import org.springframework.stereotype.Service;

@Service(value = "userService")
public class UserServiceImpl extends BasicServiceImpl<User,Long> implements UserService,UserDetailsService,SocialUserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean isNullByUsername(String username) {
        User user = userRepository.queryFirstByUsername(username);
        if (user == null){
            return true;
        }
        return false;
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.queryFirstByUsername(username);
    }

    @Override
    public User update(User user) {
        int resoult = userRepository.update(user);
        if (resoult > 0) {
            return userRepository.findOne(user.getId());
        }
        return user;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = getUser(s);
        return new LoginUser(user);
    }

    @Override
    public SocialUserDetails loadUserByUserId(String s) throws UsernameNotFoundException {
        User user = getUser(s);
        return new LoginUser(user);
    }


    private User getUser(String username){
        return userRepository.queryFirstByUsername(username);
    }

}
