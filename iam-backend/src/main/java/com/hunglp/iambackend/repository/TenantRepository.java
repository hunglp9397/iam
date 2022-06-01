package com.hunglp.iambackend.repository;

import com.hunglp.iambackend.model.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TenantRepository extends JpaRepository<Tenant, Long> {

  Optional<Tenant> findByName(String name);
}
