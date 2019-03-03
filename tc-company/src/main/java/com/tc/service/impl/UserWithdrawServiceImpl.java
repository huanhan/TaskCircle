package com.tc.service.impl;

import com.tc.controller.AuditController;
import com.tc.db.entity.UserWithdraw;
import com.tc.db.enums.WithdrawState;
import com.tc.db.repository.UserWithdrawRepository;
import com.tc.dto.TimeScope;
import com.tc.dto.finance.QueryFinance;
import com.tc.service.UserWithdrawService;
import com.tc.until.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 用户提现记录服务的实现
 * @author Cyg
 */
@Service
public class UserWithdrawServiceImpl extends AbstractBasicServiceImpl<UserWithdraw> implements UserWithdrawService {

    @Autowired
    private UserWithdrawRepository userWithdrawRepository;



    @Transactional(rollbackFor = RuntimeException.class,readOnly = true)
    @Override
    public Page<UserWithdraw> findByQueryFinance(QueryFinance queryFinance) {
        return userWithdrawRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = QueryFinance.initPredicates(queryFinance,root,query,cb);
            return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        },queryFinance);
    }

    @Transactional(rollbackFor = RuntimeException.class,readOnly = true)
    @Override
    public List<UserWithdraw> findByQueryFinanceNotPage(QueryFinance queryFinance) {
        return userWithdrawRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = QueryFinance.initPredicates(queryFinance,root,query,cb);
            return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        });
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public Boolean updateState() {
        //获取提现状态为审核中的状态，并且审核时长超过设置的审核时长
        List<UserWithdraw> uws = userWithdrawRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.or(cb.equal(root.get(UserWithdraw.STATE),WithdrawState.AUDIT_CENTER),
                    cb.equal(root.get(UserWithdraw.STATE),WithdrawState.PAY_AUDIT_CENTER)));
            predicates.add(cb.lessThan(root.get(UserWithdraw.ADMIN_AUDIT_TIME),new Timestamp(System.currentTimeMillis() - AuditController.AUDIT_LONG)));
            return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        });
        if (ListUtils.isEmpty(uws)){
            return true;
        }else {
            List<String> aIds = new ArrayList<>();
            List<String> pIds = new ArrayList<>();
            uws.forEach(userWithdraw -> {
                if (userWithdraw.getState().equals(WithdrawState.AUDIT_CENTER)){
                    aIds.add(userWithdraw.getId());
                }else if (userWithdraw.getState().equals(WithdrawState.PAY_AUDIT_CENTER)){
                    pIds.add(userWithdraw.getId());
                }
            });
            int count = 0;
            if (ListUtils.isNotEmpty(aIds)){
                count += userWithdrawRepository.updateState(aIds, WithdrawState.AUDIT);
            }
            if (ListUtils.isNotEmpty(pIds)){
                count += userWithdrawRepository.updateState(pIds, WithdrawState.PAY_AUDIT);
            }
            return count > 0;
        }
    }

    @Transactional(rollbackFor = RuntimeException.class,readOnly = true)
    @Override
    public UserWithdraw findByIdAndState(String id, WithdrawState state) {
        return userWithdrawRepository.findByIdAndState(id,state);
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public Boolean updateState(String id, WithdrawState state, Date now,Long me) {
        int count = userWithdrawRepository.updateState(id,state,new Timestamp(now.getTime()),me);
        return count > 0;
    }

    @Transactional(rollbackFor = RuntimeException.class,readOnly = true)
    @Override
    public Page<UserWithdraw> findByCashPledge(Long id, TimeScope scope) {
        scope.setId(id);
        return userWithdrawRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = QueryFinance.initPredicatesBy(scope,root,query,cb);
            return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        },scope);
    }

    @Transactional(rollbackFor = RuntimeException.class,readOnly = true)
    @Override
    public Integer countByAdmin(Long me) {
        return userWithdrawRepository.countByAdminId(me);
    }

    @Transactional(rollbackFor = RuntimeException.class,readOnly = true)
    @Override
    public UserWithdraw findOne(String id) {
        return userWithdrawRepository.findOne(id);
    }
}
