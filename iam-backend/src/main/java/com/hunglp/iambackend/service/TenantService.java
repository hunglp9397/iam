package com.hunglp.iambackend.service;

import com.hunglp.iambackend.model.Tenant;

import java.util.Optional;

public interface TenantService {

    Optional<Tenant> findByTenantId(Long tenantId);

    Optional<Tenant> findByTenantName(String name);


}
