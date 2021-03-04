package com.epam.esm.service.impl;

import com.epam.esm.service.security.JwtUser;
import org.springframework.security.core.GrantedAuthority;

public class RoleUtil {

    private static final String ADMIN_AUTHORITY = "ROLE_ADMIN";

    public static final boolean hasAdminAuthority(JwtUser jwtUser) {
        for (GrantedAuthority authority : jwtUser.getAuthorities()) {
            if (authority.getAuthority().equals(ADMIN_AUTHORITY)) {
                return true;
            }
        }
        return false;
    }
}
