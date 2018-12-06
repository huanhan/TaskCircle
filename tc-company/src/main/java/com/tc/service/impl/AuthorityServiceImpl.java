package com.tc.service.impl;

import com.tc.db.entity.Authority;
import com.tc.db.repository.AuthorityRepository;
import com.tc.service.AuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorityServiceImpl extends AbstractBasicServiceImpl<Authority> implements AuthorityService {

    @Autowired
    private AuthorityRepository authorityRepository;

    @Override
    public List<Authority> findAll() {
        return authorityRepository.findAll();
    }
}
