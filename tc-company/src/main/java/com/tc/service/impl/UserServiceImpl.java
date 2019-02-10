package com.tc.service.impl;

import com.tc.controller.AuditController;
import com.tc.db.entity.*;
import com.tc.db.entity.pk.UserImgPK;
import com.tc.db.enums.UserCategory;
import com.tc.db.enums.UserIMGName;
import com.tc.db.enums.UserState;
import com.tc.db.repository.*;
import com.tc.dto.TimeScope;
import com.tc.dto.finance.QueryFinance;
import com.tc.dto.task.QueryHunterTask;
import com.tc.dto.task.QueryTask;
import com.tc.dto.user.CashPledge;
import com.tc.dto.user.LoginUser;
import com.tc.dto.user.ModifyPassword;
import com.tc.dto.user.QueryUser;
import com.tc.exception.DBException;
import com.tc.service.UserService;
import com.tc.until.ListUtils;
import com.tc.until.StringResourceCenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.social.security.SocialUserDetails;
import org.springframework.social.security.SocialUserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
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

    @Autowired
    private UserImgRepository userImgRepository;

    @Autowired
    private UserWithdrawRepository userWithdrawRepository;

    @Autowired
    private HunterTaskRepository hunterTaskRepository;

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

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public Boolean updateState(UserState state) {
        //获取任务状态为审核中的状态，并且审核时长超过设置的审核时长
        List<User> users = userRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get(User.STATE),UserState.AUDIT_CENTER));
            predicates.add(cb.lessThan(root.get(User.ADMIN_AUDIT_TIME),new Timestamp(System.currentTimeMillis() - AuditController.AUDIT_LONG)));
            return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        });
        if (ListUtils.isEmpty(users)){
            return true;
        }else {
            List<Long> ids = User.toIds(users);
            int count = userRepository.updateState(ids,state);
            return count > 0;
        }
    }

    @Transactional(rollbackFor = RuntimeException.class,readOnly = true)
    @Override
    public User findByIdAndState(Long id, UserState state) {
        return userRepository.findByIdAndState(id,state);
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public Boolean updateState(Long id, UserState state, Date now) {
        int count = userRepository.updateState(id,state,new Timestamp(now.getTime()));
        return count > 0;
    }

    @Transactional(rollbackFor = RuntimeException.class,readOnly = true)
    @Override
    public long countByQuery(QueryUser queryUser) {
        return userRepository.count((root, query, cb) -> {
            List<Predicate> predicates = QueryUser.initPredicates(queryUser,root,query,cb);
            return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        });
    }

    @Transactional(rollbackFor = RuntimeException.class,readOnly = true)
    @Override
    public long countByIds(List<Long> lIds) {
        return userRepository.countByIdIn(lIds);
    }

    @Override
    public boolean hasPasswordEquals(String encodePassword, String password) {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.matches(password, encodePassword);
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public boolean updatePassword(Long id, ModifyPassword modifyPassword) {
        modifyPassword.setEncoderPassword(modifyPassword.getNewPassword());
        int count = userRepository.updatePassword(id,modifyPassword.getNewPassword());
        return count > 0;
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public boolean updateHeader(Long id, String header) {
        int count = userRepository.updateHeader(id,header);
        if (count > 0){
            UserImg query = userImgRepository.findOne(new UserImgPK(id,UserIMGName.VIRTUAL_HEAD));
            if (query == null){
                userImgRepository.save(new UserImg(id,UserIMGName.VIRTUAL_HEAD,header));
            }else {
                count = userImgRepository.update(header, UserIMGName.VIRTUAL_HEAD, id);
            }
        }
        return count > 0;
    }

    @Transactional(rollbackFor = RuntimeException.class,readOnly = true)
    @Override
    public List<CashPledge> findByCashPledgeAndUser(TimeScope scope) {
        User user = userRepository.findOne(scope.getId());
        if (user == null){
            throw new DBException(StringResourceCenter.DB_QUERY_FAILED);
        }
        List<Task> tasks = taskRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = QueryTask.initPredicatesBy(scope,root,query,cb);
            return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        });
        List<UserWithdraw> userWithdraws = userWithdrawRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = QueryFinance.initPredicatesBy(scope,root,query,cb);
            return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        });
        List<HunterTask> hunterTasks = null;
        if (user.getCategory().equals(UserCategory.HUNTER)){
            hunterTasks = hunterTaskRepository.findAll((root, query, cb) -> {
                List<Predicate> predicates = QueryHunterTask.initPredicatesBy(scope,root,query,cb);
                return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
            });
        }
        return CashPledge.create(tasks,userWithdraws,hunterTasks);
    }

    @Override
    public Long loadId(String account) {
        return userRepository.loadId(account);
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

    @Transactional(rollbackFor = RuntimeException.class,readOnly = true)
    @Override
    public long count() {
        return userRepository.count();
    }


}
