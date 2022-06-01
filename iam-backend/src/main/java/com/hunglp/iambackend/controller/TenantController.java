package com.hunglp.iambackend.controller;


import com.hunglp.iambackend.model.Tenant;
import com.hunglp.iambackend.service.TenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class TenantController {

    @Autowired
    private TenantService tenantService;

    @GetMapping("/tenant/{tenantId}")
    public Optional<Tenant> getTenant(@PathVariable Long tenantId){
        return tenantService.findByTenantId(tenantId);
    }



}
