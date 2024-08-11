package com.majlo.antares.enums;

import org.springframework.security.core.GrantedAuthority;

public enum MainRole implements GrantedAuthority {
    ADMIN,
    EVENT_MANAGER,
    USER;

    @Override
    public String getAuthority() {
        return this.name();
    }
}
