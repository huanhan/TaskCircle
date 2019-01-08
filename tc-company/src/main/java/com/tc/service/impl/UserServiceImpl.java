package com.tc.service.impl;

import com.tc.db.entity.Task;
import com.tc.db.entity.User;
import com.tc.db.repository.TaskRepository;
import com.tc.db.repository.UserRepository;
import com.tc.dto.authority.QueryAuthority;
import com.tc.dto.user.LoginUser;
import com.tc.dto.user.QueryUser;
import com.tc.exception.DBException;
import com.tc.service.UserService;
import com.tc.until.StringResourceCenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.social.security.SocialUserDetails;
import org.springframework.social.security.SocialUserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import java.util.List;

/**
 * @author Cyg
 */
@Service(value = "userService")
public class UserServiceImpl extends AbstractBasicServiceImpl<User> implements UserService,UserDetailsService,SocialUserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Transactional(rollbackFor = RuntimeException.class,readOnly = true)
    @Override
    public boolean isNullByUsername(String username) {
        User user = userRepository.queryFirstByUsername(username);
        if (user == null){
            return true;
        }
        return false;
    }

    @Transactional(rollbackFor = RuntimeException.class,readOnly = true)
    @Override
    public User getUserByUsername(String username) {
        User user = userRepository.queryFirstByUsername(username);
        if (user == null) {
            throw new DBException(StringResourceCenter.DB_QUERY_FAILED);
        }
        return user;
    }


    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public User update(User user) {
        int result = userRepository.update(user);
        if (result > 0) {
            return getUserByUsername(user.getUsername());
        }
        return user;
    }

    @Transactional(rollbackFor = RuntimeException.class,readOnly = true)
    @Override
    public Long getIdByUsername(String username) {
        User user = getUserByUsername(username);
        return user.getId();
    }

    @Transactional(rollbackFor = RuntimeException.class,readOnly = true)
    @Override
    public Page<User> findByQueryUser(QueryUser queryUser) {


        return userRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = QueryUser.initPredicates(queryUser,root,query,cb);
            return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        },queryUser);
    }

    @Transactional(rollbackFor = RuntimeException.class,readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = getUser(s);
        return new LoginUser(user);
    }

    @Transactional(rollbackFor = RuntimeException.class,readOnly = true)
    @Override
    public SocialUserDetails loadUserByUserId(String s) throws UsernameNotFoundException {
        User user = getUser(s);
        return new LoginUser(user);
    }

    @Transactional(rollbackFor = RuntimeException.class,readOnly = true)
    User getUser(String username){
        return userRepository.queryFirstByUsername(username);
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Transactional(rollbackFor = RuntimeException.class,readOnly = true)
    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Transactional(rollbackFor = RuntimeException.class,readOnly = true)
    @Override
    public List<User> findAll(Sort sort) {
        return userRepository.findAll(sort);
    }

    @Transactional(rollbackFor = RuntimeException.class,readOnly = true)
    @Override
    public User findOne(Long id) {
        return userRepository.findOne(id);
    }
}
