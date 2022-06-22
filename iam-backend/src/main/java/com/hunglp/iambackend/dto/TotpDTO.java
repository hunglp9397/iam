package com.hunglp.iambackend.dto;

import lombok.Data;

@Data
public class TotpDTO {
    private String code;
    private String secret;
}
