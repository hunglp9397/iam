package com.hunglp.iambackend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.tomcat.jni.Address;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Users extends DateAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long userId;

    private String username;

    private String password;

    private String authenticationType;

    private String secretCode;

    private boolean isDeleted;

    @ManyToOne
    @JoinColumn(name = "tenantId",nullable = false)
    private Tenant tenant;

    @OneToOne(cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private Role role;

    @OneToOne(mappedBy = "user")
    private UserDetail userDetail;


}
