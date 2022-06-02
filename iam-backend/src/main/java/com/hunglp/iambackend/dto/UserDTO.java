package com.hunglp.iambackend.dto;


import lombok.Data;
import org.hibernate.validator.constraints.Length;


@Data
public class UserDTO {

    @Length(max = 3,min = 1, message = "invalid username")
    private String username;

    @Length(max = 3,min = 1, message = "invalid password")
    private String password;

    private String email;

    private String firstName;

    private String lastName;

    private int statusCode;

    private String status;
}
