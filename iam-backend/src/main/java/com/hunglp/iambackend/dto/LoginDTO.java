package com.hunglp.iambackend.dto;

import lombok.Data;

@Data
public class LoginDTO {

    private String username;

    private String password;

    private String tenantId;

    private boolean isDeleted;
}
