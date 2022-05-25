package com.hunglp.iambackend.dto;


import lombok.Data;

@Data
public class UserDTO {

    private String username;

    private String password;

    private String email;

    private String firstName;

    private String lastName;

    private int statusCode;

    private String status;
}
