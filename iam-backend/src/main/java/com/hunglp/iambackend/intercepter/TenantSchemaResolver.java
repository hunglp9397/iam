package com.hunglp.iambackend.intercepter;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;

public class TenantSchemaResolver implements CurrentTenantIdentifierResolver {

    private String defaultTenant = "public";

    @Override
    public String resolveCurrentTenantIdentifier() {
        String t = TenantContext.getCurrentTenant();
        return t != null ? t : defaultTenant;
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return true;
    }
}
