package com.delivery.deliveryservice.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public final class AuthUtil {

    private AuthUtil() {
    }
    public static Optional<UserDetailsImpl> getCurrentUserDetails() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(securityContext.getAuthentication())
                .map(Authentication::getPrincipal)
                .filter(UserDetailsImpl.class::isInstance)
                .map(UserDetailsImpl.class::cast);
    }

    public static String getCurrentUserLogin() {
        if (getCurrentUserDetails().isPresent()) {
            return getCurrentUserDetails().get().getUsername();
        }
        return null;
    }

     public static boolean hasAdminRole() {
        if (getCurrentUserDetails().isPresent()) {
            return getCurrentUserDetails().get().getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }
        return false;
    }

    public static boolean hasCourierRole() {
        if (getCurrentUserDetails().isPresent()) {
            return getCurrentUserDetails().get().getAuthorities().contains(new SimpleGrantedAuthority("ROLE_COURIER"));
        }
        return false;
    }

}
