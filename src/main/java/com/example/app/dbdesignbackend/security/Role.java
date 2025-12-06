package com.example.app.dbdesignbackend.security;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    STUDENT("STUDENT"),
    TEACHER("TEACHER"),
    ADMIN("ADMIN");

    private final String role;

    Role(String role) {
        this.role = role;
    }

    @Override
    public String getAuthority() {
        return role;
    }

    public static  Role fromString(String role) {
        for (Role r : Role.values()) {
            if (r.role.equalsIgnoreCase(role)) {
                return r;
            }
        }
        throw new IllegalArgumentException("No role found for: " + role);
    }
}
