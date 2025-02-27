package com.barogo.assignment.api.global.security.jwt;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Arrays;

import static java.lang.String.format;

public enum AuthorityRoleType implements GrantedAuthority {

    ORDER_USER("ORDER_USER", 10),
    DELIVER_USER("DELIVER_USER", 20);

    String role;

    @Getter
    Integer code;

    AuthorityRoleType(String role, Integer code) {
        this.role = role;
        this.code = code;
    }

    public static AuthorityRoleType ofCode(Integer code) {
        return Arrays.stream(AuthorityRoleType.values())
                .filter(r -> r.getCode().equals(code))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(format("Authority role type not exists: %s (code)", code)));
    }

    public static AuthorityRoleType valueOfRole(String roleName) {
        return Arrays.stream(values()).filter(item -> item.role.equals(roleName)).findAny().orElse(null);
    }

    @Override
    public String getAuthority() {
        return this.role;
    }

}
