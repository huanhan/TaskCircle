package com.tc.controller;

import com.tc.db.entity.Authority;
import com.tc.db.entity.Resource;
import com.tc.dto.Show;
import com.tc.service.AuthorityService;
import com.tc.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@ResponseStatus(code = HttpStatus.OK)
@RequestMapping(value = "/authority")
public class AuthorityController {

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private AuthorityService authorityService;

    public void all(Authentication authentication){
        List<Show> showResources = new ArrayList<>();
        List<Show> showAuthority = new ArrayList<>();
        List<Resource> resources = resourceService.findAll();
        if (!resources.isEmpty()){
            resources.forEach(resource ->
                showResources.add(new Show(resource.getId(),resource.getName()))
            );
        }
        List<Authority> authorities = authorityService.findAll();
        if (!authorities.isEmpty()){
            authorities.forEach(authority ->
                showAuthority.add(new Show(authority.getId(),authority.getName()))
            );
        }




    }
}
