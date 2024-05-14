package ua.wms.warehouse.security;

import org.springframework.security.core.GrantedAuthority;

public enum Authority implements GrantedAuthority {

    ROLE_CLIENT,
    ROLE_ADMIN,
    ROLE_EMPLOYEE;

    @Override
    public String getAuthority() {
        return name();
    }

}
