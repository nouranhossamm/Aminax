package com.animax.animax.servicesImpl;

import com.animax.animax.dto.JwtAuthenticationResponse;
import com.animax.animax.dto.RefreshTokenRequest;
import com.animax.animax.dto.SigninRequest;
import com.animax.animax.dto.SignupRequest;
import com.animax.animax.entities.User;
import com.animax.animax.mapper.UserMapper;
import com.animax.animax.services.AuthenticationService;
import com.animax.animax.repositories.UserRepository;
import com.animax.animax.services.JWTService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;
    private final JWTService jwtService;

    public User signup(SignupRequest signupRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorMessageBuilder = new StringBuilder("Validation failed. Errors: ");
            for (FieldError error : bindingResult.getFieldErrors()) {
                errorMessageBuilder.append(error.getDefaultMessage()).append("; ");
            }
            throw new IllegalArgumentException(errorMessageBuilder.toString());
        }

        try {
            User user = userMapper.signUpRequestToEntity(signupRequest);
            user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));

            User savedUser = userRepository.save(user);
            var jwtToken = jwtService.generateToken(savedUser);

//            var jwtRefreshToken = jwtService.generateRefreshToken( savedUser);

            // You can log the tokens or handle them as needed, but do not return them with the User object
            // Log or store the tokens as necessary

            return savedUser;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("An error occurred while processing the signup request: " + e.getMessage());
        }
    }

    public JwtAuthenticationResponse signin(SigninRequest signinRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        signinRequest.getEmail(),
                        signinRequest.getPassword())
        );
        var user = userRepository.findByEmail(signinRequest.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid Username or password"));

        var jwt = jwtService.generateToken(user);

        var refreshToken = jwtService.generateRefreshToken(new HashMap<>(), user);

        JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();
        jwtAuthenticationResponse.setToken(jwt);
        jwtAuthenticationResponse.setRefreshToken(refreshToken);

        return jwtAuthenticationResponse;
    }


    public JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
            String userEmail = jwtService.extractUserName(refreshTokenRequest.getToken());
            User user = userRepository.findByEmail(userEmail).orElseThrow(() ->
                    new IllegalArgumentException("User not found"));

            if (jwtService.isTokenValid(refreshTokenRequest.getToken(), user)) {
                var jwt = jwtService.generateToken(user);

                JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();
                jwtAuthenticationResponse.setToken(jwt);
                jwtAuthenticationResponse.setRefreshToken(refreshTokenRequest.getToken());

                return jwtAuthenticationResponse;
            }

            return null;
    }

}
