package com.tc.service.impl;

import com.tc.controller.AuditController;
import com.tc.db.entity.User;
import com.tc.db.entity.UserWithdraw;
import com.tc.db.enums.UserState;
import com.tc.db.enums.WithdrawState;
import com.tc.db.repository.UserWithdrawRepository;
import com.tc.dto.finance.QueryFinance;
import com.tc.dto.user.QueryUser;
import com.tc.service.UserWithdrawService;
import com.tc.until.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户提现记录服务的实现
 * @author Cyg
 */
@Service
public class UserWithdrawServiceImpl extends AbstractBasicServiceImpl<UserWithdraw> implements UserWithdrawService {

    @Autowired
    private UserWithdrawRepository userWithdrawRepository;

    @Override
    public Page<UserWithdraw> findByQueryFinance(QueryFinance queryFinance) {
        return userWithdrawRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = QueryFinance.initPredicates(queryFinance,root,query,cb);
            return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        },queryFinance);
    }

    @Override
    public Boolean updateState(WithdrawState state) {
        //获取任务状态为审核中的状态，并且审核时长超过设置的审核时长
        List<UserWithdraw> uws = userWithdrawRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get(UserWithdraw.STATE),UserState.AUDIT_CENTER));
            predicates.add(cb.lessThan(root.get(UserWithdraw.ADMIN_AUDIT_TIME),new Timestamp(System.currentTimeMillis() - AuditController.AUDIT_LONG)));
            return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        });
        if (ListUtils.isEmpty(uws)){
            return true;
        }else {
            List<String> ids = UserWithdraw.toIds(uws);
            int count = userWithdrawRepository.updateState(ids,state);
            return count > 0;
        }
    }
}
