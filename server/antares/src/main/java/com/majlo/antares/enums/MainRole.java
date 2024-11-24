package com.majlo.antares.enums;

import org.springframework.security.core.GrantedAuthority;

public enum MainRole implements GrantedAuthority {
    ADMIN,
    EVENT_OWNER,
    TICKET_CONTROLLER,
    USER;

    @Override
    public String getAuthority() {
        return this.name();
    }
}
