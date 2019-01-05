package com.tc.service.impl;

import com.tc.db.entity.Admin;
import com.tc.db.entity.AdminAuthority;
import com.tc.db.entity.User;
import com.tc.db.repository.AdminRepository;
import com.tc.dto.admin.QueryAdmin;
import com.tc.dto.enums.AuthorityState;
import com.tc.service.AdminService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

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

    @Override
    public Page<Admin> findByQueryAdminAndAuthority(QueryAdmin queryAdmin, Long authority) {
        return adminRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = QueryAdmin.initPredicates(queryAdmin,root,query,cb);
            predicates.add(cb.equal(root.join(Admin.ADMIN_AUTHORITIES).get(AdminAuthority.AUTHORITY_ID),authority));
            return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        },queryAdmin);
    }

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
}
