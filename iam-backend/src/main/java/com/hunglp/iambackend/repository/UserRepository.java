package com.hunglp.iambackend.repository;

import com.hunglp.iambackend.dto.LoginDTO;
import com.hunglp.iambackend.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends  JpaRepository<Users, Long> {

    @Query("select u from Users u where u.username = ?1 and  u.password=?2 and u.password=?3 and u.isDeleted=?4")
    Optional<Users> findAccount(String username, String password, String tenantId, boolean isDeleted);
}
