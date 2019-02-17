package com.tc.service.impl;

import com.tc.db.entity.Admin;
import com.tc.db.entity.AdminAuthority;
import com.tc.db.entity.User;
import com.tc.db.enums.AdminState;
import com.tc.db.repository.AdminRepository;
import com.tc.db.repository.UserRepository;
import com.tc.dto.admin.QueryAdmin;
import com.tc.dto.enums.AuthorityState;
import com.tc.service.AdminService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * 管理员服务的实现
 * @author Cyg
 */
@Service
public class AdminServiceImpl extends AbstractBasicServiceImpl<Admin> implements AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional(rollbackFor = RuntimeException.class,readOnly = true)
    @Override
    public Page<Admin> findByQueryAdminAndAuthority(QueryAdmin queryAdmin, Long authority) {
        return adminRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = QueryAdmin.initPredicates(queryAdmin,root,query,cb);
            predicates.add(cb.equal(root.join(Admin.ADMIN_AUTHORITIES).get(AdminAuthority.AUTHORITY_ID),authority));
            //predicates.add(cb.equal(root.get(Admin.CREATE_ID),queryAdmin.getCreation()));
            return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        },queryAdmin);
    }

    @Transactional(rollbackFor = RuntimeException.class,readOnly = true)
    @Override
    public List<Admin> findByQueryAdminAndAuthorityState(QueryAdmin queryAdmin) {
        return adminRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = QueryAdmin.initPredicates(queryAdmin,root,query,cb);
            if (queryAdmin.isAuthority() != null) {
                if (!queryAdmin.isAuthority()) {
                    predicates.add(cb.isEmpty(root.get(Admin.ADMIN_AUTHORITIES)));
                } else  {
                    predicates.add(cb.isNotEmpty(root.get(Admin.ADMIN_AUTHORITIES)));
                }
            }
            return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        });
    }

    @Transactional(rollbackFor = RuntimeException.class,readOnly = true)
    @Override
    public Page<Admin> findByQueryAdmin(QueryAdmin queryAdmin) {
        return adminRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = QueryAdmin.initPredicates(queryAdmin,root,query,cb);
            return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        },queryAdmin);
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public boolean leaveOffice(Long id) {
        int count = adminRepository.updateByAdminState(AdminState.LEAVE_FOOICE,id);
        return count == 1;
    }

    @Transactional(rollbackFor = RuntimeException.class,readOnly = true)
    @Override
    public List<Admin> findByNotAuthority(Long id) {
        return adminRepository.findByNotAuthority(id);
    }

    @Override
    public List<Admin> findByNotAuthority(Long id, Long creation) {
        return adminRepository.findByNotAuthority(id,creation);
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public Admin save(Admin admin) {
        User user = userRepository.save(admin.getUser());
        admin.setUserId(user.getId());
        return adminRepository.save(admin);
    }

    @Transactional(rollbackFor = RuntimeException.class,readOnly = true)
    @Override
    public Admin findOne(Long id) {
        return adminRepository.findOne(id);
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public Admin update(Admin user) {
        return adminRepository.saveAndFlush(user);
    }

    @Transactional(rollbackFor = RuntimeException.class,readOnly = true)
    @Override
    public List<Admin> findByIds(List<Long> ids) {
        return adminRepository.findByUserIdIn(ids);
    }
}
