package com.hunglp.iambackend.controller;

import com.hunglp.iambackend.dto.TotpDTO;
import com.hunglp.iambackend.service.TotpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class TotpController {

    @Autowired
    private TotpService totpService;

    @GetMapping("/getqrcode")
    public String getQRCode() {
        String secret = totpService.generateSecret();
        System.out.println(secret);
        return totpService.getUriForImage(secret);
    }

    @PostMapping("/verifyOTP")
    public String verifyOTP(@RequestBody TotpDTO totpDTO) {
        boolean isVerified = totpService.verifyCode(totpDTO.getCode(), totpDTO.getSecret());
        return isVerified ? "OK" : "FAIL";
    }

}
