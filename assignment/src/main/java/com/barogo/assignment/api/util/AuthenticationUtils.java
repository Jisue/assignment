package com.barogo.assignment.api.util;

import com.barogo.assignment.api.global.security.jwt.BarogoUser;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public class AuthenticationUtils {

    public static Optional<BarogoUser> currentUser() {
        SecurityContext context = SecurityContextHolder.getContext();

        Authentication authentication = context.getAuthentication();
        if (ObjectUtils.isEmpty(authentication) || !authentication.isAuthenticated()) {
            return Optional.empty();
        }

        Object principal = authentication.getPrincipal();
        if (ObjectUtils.isEmpty(principal) || !(principal instanceof UserDetails)) {
            return Optional.empty();
        }

        return Optional.of((BarogoUser) principal);
    }

    public static Optional<String> currentUserId() {
        Optional<BarogoUser> user = currentUser();
        return user.map(BarogoUser::getUserId);
    }

}
