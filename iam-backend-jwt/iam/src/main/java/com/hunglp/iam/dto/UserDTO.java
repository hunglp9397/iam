package com.hunglp.iam.dto;

import lombok.Data;

@Data
public class UserDTO {
    private String username;
    private String password;
    private String tenantId;
}
