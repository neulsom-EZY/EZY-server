package com.server.EZY.model.user;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ROLE_ADMIN, ROLE_CLIENT, ROLE_NOT_PERMIT;

    public String getAuthority(){
        return name();
    }
}
