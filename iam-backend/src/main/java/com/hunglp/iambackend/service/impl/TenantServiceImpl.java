package com.hunglp.iambackend.service.impl;

import com.hunglp.iambackend.exception.ResourceNotFoundException;
import com.hunglp.iambackend.model.Tenant;
import com.hunglp.iambackend.repository.TenantRepository;
import com.hunglp.iambackend.repository.UserRepository;
import com.hunglp.iambackend.service.TenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TenantServiceImpl implements TenantService {

    private final TenantRepository tenantRepository;

    public TenantServiceImpl(final TenantRepository tenantRepository){
        this.tenantRepository=tenantRepository;
    }

    @Override
    public Optional<Tenant> findByTenantId(Long tenantId) {
        return tenantRepository.findById(tenantId);
    }

    @Override
    public Optional<Tenant> findByTenantName(String name) {
       return Optional.ofNullable(tenantRepository.findByName(name).orElseThrow(() -> new ResourceNotFoundException("Not found Tenant with name = " + name)));
    }
}
