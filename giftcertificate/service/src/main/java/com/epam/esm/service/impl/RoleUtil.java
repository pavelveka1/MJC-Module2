package com.epam.esm.service.impl;

import com.epam.esm.service.constant.ServiceConstant;
import com.epam.esm.service.security.JwtUser;
import org.springframework.security.core.GrantedAuthority;

public class RoleUtil {

    public static final boolean hasAdminAuthority(JwtUser jwtUser) {
        for (GrantedAuthority authority : jwtUser.getAuthorities()) {
            if (authority.getAuthority().equals(ServiceConstant.ADMIN_AUTHORITY)) {
                return true;
            }
        }
        return false;
    }
}
