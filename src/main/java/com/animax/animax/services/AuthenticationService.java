package com.animax.animax.services;

import com.animax.animax.dto.JwtAuthenticationResponse;
import com.animax.animax.dto.RefreshTokenRequest;
import com.animax.animax.dto.SigninRequest;
import com.animax.animax.dto.SignupRequest;
import com.animax.animax.entities.User;
import org.springframework.stereotype.Repository;
import org.springframework.validation.BindingResult;

@Repository
public interface AuthenticationService {
    User signup(SignupRequest signUpRequest, BindingResult bindingResult);
    JwtAuthenticationResponse signin(SigninRequest signinRequest);
    JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest);
}