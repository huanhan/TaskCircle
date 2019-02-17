package com.tc.security;

import com.tc.db.entity.Authority;
import com.tc.db.entity.AuthorityResource;
import com.tc.db.entity.User;
import com.tc.db.enums.UserCategory;
import com.tc.dto.user.LoginUser;
import com.tc.service.AdminService;
import com.tc.service.AuthorityService;
import com.tc.service.UserService;
import com.tc.until.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component("rbacService")
public class RbacServiceImpl implements RbacService {

    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Autowired
    private AuthorityService authorityService;

    @Autowired
    private UserService userService;

    @Override
    public boolean hasPermission(HttpServletRequest request, Authentication authentication) {
        Collection<? extends GrantedAuthority> account = authentication.getAuthorities();

        List<Long> ids = new ArrayList<>();

        User user = userService.findByUsername((String)authentication.getPrincipal());

        if (user.getCategory().equals(UserCategory.SUPPER_ADMIN)){
            return true;
        }

        account.forEach(o -> ids.add(Long.parseLong(o.getAuthority())));

        Collection<Authority> authorities = authorityService.findByIds(ids);

        if (ListUtils.isNotEmpty(authorities)){
            for (Authority authority : authorities) {
                if (ListUtils.isNotEmpty(authority.getAuthorityResources())){
                    for (AuthorityResource authorityResource : authority.getAuthorityResources()) {
                        if (antPathMatcher.match(authorityResource.getResource().getPath(),request.getRequestURI())){
                            return true;
                        }
                    }
                }
            }
        }

        return false;

    }
}
