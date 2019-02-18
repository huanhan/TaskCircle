package com.tc.service.impl;

import com.tc.db.entity.UserAuthority;
import com.tc.db.enums.UserCategory;
import com.tc.db.repository.UserAuthorityRepository;
import com.tc.dto.authority.RemoveUser;
import com.tc.exception.DBException;
import com.tc.exception.ValidException;
import com.tc.service.UserAuthorityService;
import com.tc.until.ListUtils;
import com.tc.until.StringResourceCenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 用户与权限关系服务的实现
 * @author Cyg
 */
@Service
public class UserAuthorityServiceImpl extends AbstractBasicServiceImpl<UserAuthority> implements UserAuthorityService {
    @Autowired
    private UserAuthorityRepository userAuthorityRepository;

    @Transactional(rollbackFor = RuntimeException.class,readOnly = true)
    @Override
    public List<UserAuthority> findByAuthorityId(Long authorityId) {
        return userAuthorityRepository.findByAuthorityIdEquals(authorityId);
    }

    @Transactional(rollbackFor = RuntimeException.class,readOnly = true)
    @Override
    public List<UserAuthority> findAll() {
        return userAuthorityRepository.findAll(new Sort(Sort.Direction.DESC,UserAuthority.CATEGORY));
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public boolean deleteByIds(RemoveUser ids) {
        return userAuthorityRepository.deleteByCategoryIsInAndAuthorityIdEquals(ids.getIds(),ids.getId()) == ids.getIds().size();
    }

    @Transactional(rollbackFor = RuntimeException.class,readOnly = true)
    @Override
    public List<UserAuthority> findByUserCategory(UserCategory userCategory) {
        return userAuthorityRepository.findByCategoryEquals(userCategory);
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public List<UserAuthority> save(List<UserAuthority> userAuthorities) {
        return userAuthorityRepository.save(userAuthorities);
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public boolean deleteByAuthorityIds(List<Long> ids,UserCategory userCategory) {
        int count = userAuthorityRepository.deleteByAuthorityIdIsInAndCategoryEquals(ids,userCategory);
        return count == ids.size();
    }

    @Transactional(rollbackFor = RuntimeException.class,readOnly = true)
    @Override
    public List<UserAuthority> findBy(Long id, List<UserCategory> ids) {
        return userAuthorityRepository.findByCategoryInAndAuthorityIdEquals(ids,id);
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public Boolean saveNewsAndRemoveOldes(List<UserAuthority> news, List<Long> old, UserCategory key) {

        if (ListUtils.isNotEmpty(news)){
            List<UserAuthority> result = userAuthorityRepository.save(news);
            if (result.size() != news.size()){
                throw new DBException(StringResourceCenter.DB_INSERT_FAILED);
            }
        }else {
            if (ListUtils.isEmpty(old)){
                throw new ValidException("无意义的操作");
            }
        }

        int result = userAuthorityRepository.deleteByAuthorityIdIsInAndCategoryEquals(old,key);
        if (result != old.size()){
            throw new DBException(StringResourceCenter.DB_DELETE_FAILED);
        }

        return true;
    }


}
