package com.animax.animax.dto.requests;

import lombok.Data;

@Data
public class SignupRequest {

    private String username;
    private String email;
    private String phoneNumber;
    private String password;

}