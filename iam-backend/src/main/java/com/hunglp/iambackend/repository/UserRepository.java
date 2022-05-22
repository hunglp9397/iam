package com.hunglp.iambackend.repository;

import com.hunglp.iambackend.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends  JpaRepository<Users, Long> {
}
