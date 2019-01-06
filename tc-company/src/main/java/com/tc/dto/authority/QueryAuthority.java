package com.tc.dto.authority;

import com.tc.db.entity.Authority;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * 查询权限
 * @author Cyg
 */
public class QueryAuthority extends PageRequest {
    private String authorityName;
    private String info;
    private Timestamp begin;
    private Timestamp end;
    private String username;
    private Long adminId;

    public QueryAuthority() {
        super(0, 10);
    }

    public QueryAuthority(int page, int size) {
        super(page, size);
    }

    public QueryAuthority(int page, int size, Sort.Direction direction, String... properties) {
        super(page, size, direction, properties);
    }

    public QueryAuthority(int page, int size, Sort sort) {
        super(page, size, sort);
    }

    public String getAuthorityName() {
        return authorityName;
    }

    public void setAuthorityName(String authorityName) {
        this.authorityName = authorityName;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Timestamp getBegin() {
        return begin;
    }

    public void setBegin(Timestamp begin) {
        this.begin = begin;
    }

    public Timestamp getEnd() {
        return end;
    }

    public void setEnd(Timestamp end) {
        this.end = end;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }

    public static List<Predicate> initPredicates(QueryAuthority queryAuthority, Root<Authority> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

        List<Predicate> predicates = new ArrayList<>();

        if (!StringUtils.isEmpty(queryAuthority.getAuthorityName())){
            predicates.add(cb.like(root.get("name"),"%" + queryAuthority.getAuthorityName() + "%"));
        }
        if (!StringUtils.isEmpty(queryAuthority.getInfo())){
            predicates.add(cb.like(root.get("info"),"%" + queryAuthority.getInfo() + "%"));
        }
        if (queryAuthority.getBegin() != null || queryAuthority.getEnd() != null){
            if (queryAuthority.getBegin() != null && queryAuthority.getEnd() != null){
                predicates.add(cb.between(root.get("createTime"),queryAuthority.getBegin(),queryAuthority.getEnd()));
            }else if (queryAuthority.getBegin() != null){
                predicates.add(cb.greaterThan(root.get("createTime"),queryAuthority.getBegin()));
            }else if (queryAuthority.getEnd() != null){
                predicates.add(cb.lessThan(root.get("createTime"),queryAuthority.getEnd()));
            }
        }
        if (!StringUtils.isEmpty(queryAuthority.getUsername())){
            predicates.add(cb.equal(root.get("admin").get("user").get("username"),queryAuthority.getUsername()));
        }

        return predicates;
    }
}
