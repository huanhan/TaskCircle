package com.tc.service.impl;

import com.tc.db.entity.AdminAuthority;
import com.tc.db.repository.AdminAuthorityRepository;
import com.tc.dto.LongIds;
import com.tc.dto.admin.QueryAdmin;
import com.tc.service.AdminAuthorityService;
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
    public List<AdminAuthority> save(List<AdminAuthority> adminAuthority) {
        return adminAuthorityRepository.save(adminAuthority);
    }
}
