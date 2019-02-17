package com.tc.service.impl;

import com.tc.db.entity.AdminAuthority;
import com.tc.db.repository.AdminAuthorityRepository;
import com.tc.dto.LongIds;
import com.tc.dto.admin.QueryAdmin;
import com.tc.exception.DBException;
import com.tc.exception.ValidException;
import com.tc.service.AdminAuthorityService;
import com.tc.until.ListUtils;
import com.tc.until.StringResourceCenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * 管理员与权限关系服务的实现
 * @author Cyg
 */
@Service
public class AdminAuthorityServiceImpl extends AbstractBasicServiceImpl<AdminAuthority> implements AdminAuthorityService {

    @Autowired
    private AdminAuthorityRepository adminAuthorityRepository;

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public boolean deleteByIds(LongIds ids) {
        return adminAuthorityRepository.deleteByUserIdIsInAndAuthorityIdEquals(ids.getIds(),ids.getId()) == ids.getIds().size();
    }

    @Transactional(rollbackFor = RuntimeException.class,readOnly = true)
    @Override
    public Page<AdminAuthority> findByQueryAdmin(QueryAdmin queryAdmin) {
        return adminAuthorityRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = QueryAdmin.initPredicatesByAdminAuthority(queryAdmin,root,query,cb);
            return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        },queryAdmin);
    }

    @Transactional(rollbackFor = RuntimeException.class,readOnly = true)
    @Override
    public List<AdminAuthority> findByKeys(List<Long> admins, List<Long> authorities) {
        return adminAuthorityRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (!admins.isEmpty()){
                predicates.add(root.get(AdminAuthority.USER_ID).in(admins));
            }
            if (!authorities.isEmpty()){
                predicates.add(root.get(AdminAuthority.AUTHORITY_ID).in(authorities));
            }

            return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();

        });
    }

    @Transactional(rollbackFor = RuntimeException.class,readOnly = true)
    @Override
    public List<AdminAuthority> findByAdminId(Long id) {
        return adminAuthorityRepository.findByUserIdEquals(id);
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public boolean deleteByAuthorityIds(List<Long> authorityIds, Long id) {
        int count = adminAuthorityRepository.deleteByAuthorityIdIsInAndUserIdEquals(authorityIds,id);
        return count > 0;
    }

    @Transactional(rollbackFor = RuntimeException.class,readOnly = true)
    @Override
    public List<AdminAuthority> findBy(Long id, List<Long> ids) {
        return adminAuthorityRepository.findByUserIdInAndAuthorityIdEquals(ids,id);
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public Boolean saveNewsAndRemoveOld(List<AdminAuthority> news, List<Long> old, Long id) {
        if (ListUtils.isNotEmpty(news)){
            List<AdminAuthority> result = adminAuthorityRepository.save(news);
            if (result.size() != news.size()){
                throw new DBException(StringResourceCenter.DB_INSERT_FAILED);
            }
        }else {
            if (ListUtils.isEmpty(old)){
                throw new ValidException("无意义的操作");
            }
        }

        int result = adminAuthorityRepository.deleteByAuthorityIdIsInAndUserIdEquals(old,id);
        if (result != old.size()){
            throw new DBException(StringResourceCenter.DB_DELETE_FAILED);
        }

        return true;
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public List<AdminAuthority> save(List<AdminAuthority> adminAuthority) {
        return adminAuthorityRepository.save(adminAuthority);
    }
}
