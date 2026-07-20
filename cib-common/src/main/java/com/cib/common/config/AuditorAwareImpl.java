package com.cib.common.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

/**
 * Supplies the "current user" for @CreatedBy / @LastModifiedBy audit fields.
 * Reads the authenticated principal from Spring Security context; falls back
 * to "SYSTEM" for service-to-service (Feign) calls or when security is not
 * yet configured for a given service.
 */
public class AuditorAwareImpl implements AuditorAware<String> {

    private static final String SYSTEM_USER = "SYSTEM";

    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getPrincipal())) {
            return Optional.of(SYSTEM_USER);
        }
        return Optional.of(authentication.getName());
    }
}
