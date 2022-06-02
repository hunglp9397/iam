package com.hunglp.iambackend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.tomcat.jni.Address;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Users extends DateAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userId;

    private String username;

    private String password;

    private String authenticationType;

    private String secretCode;

    @Column(nullable = false, columnDefinition = "TINYINT", length = 1)

    private boolean isDeleted;

    @Column(nullable = false, columnDefinition = "TINYINT", length = 1)
    private boolean isEnabled;

    @ManyToOne
    @JoinColumn(name = "tenantId",nullable = false)
    private Tenant tenant;

    @OneToOne(cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private Role role;

    @OneToOne(mappedBy = "user")
    private UserDetail userDetail;


}
