package com.animax.animax.controllers;

import com.animax.animax.dto.JwtAuthenticationResponse;
import com.animax.animax.dto.RefreshTokenRequest;
import com.animax.animax.dto.SigninRequest;
import com.animax.animax.dto.SignupRequest;
import com.animax.animax.entities.User;
import com.animax.animax.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @GetMapping("/")
    public Map<String, Object> home(@AuthenticationPrincipal OAuth2User principal) {
        Map<String, Object> response = new HashMap<>();
        if (principal != null) {
            response.put("name", principal.getAttribute("name"));
            response.put("email", principal.getAttribute("email"));
        } else {
            response.put("message", "User not authenticated");
        }
        return response;
    }

    @RequestMapping("user")
    public Principal user(Principal principal) {
        return principal;
    }

    @GetMapping("login")
    public String welcome() {
        return "Hi";
    }

    @GetMapping("profile")
    public String profile(OAuth2AuthenticationToken token, Model model) {
        model.addAttribute("name", token.getPrincipal().getAttribute("name"));
        model.addAttribute("email", token.getPrincipal().getAttribute("email"));
        model.addAttribute("photo", token.getPrincipal().getAttribute("picture"));
        return "user-profile";
    }

//@RequestMapping("user")
//public Principal user(Principal principal) {
//    System.out.println(principal.getName());
//    return principal;
//}

//    @RequestMapping("user")
//    public String user(OAuth2User user) {
//        System.out.println(user.getAttributes());
//        return String.valueOf(user.getAttributes());
//    }


    @PostMapping("signup")
    public ResponseEntity<User> signup(@Validated @RequestBody SignupRequest signupRequest, BindingResult bindingResult) {
        return ResponseEntity.ok(authenticationService.signup(signupRequest, bindingResult));
    }

    @PostMapping("signin")
    public ResponseEntity<JwtAuthenticationResponse> signin(@RequestBody SigninRequest signinRequest)  {
        return ResponseEntity.ok(authenticationService.signin(signinRequest));
    }

    @PostMapping("refresh-token")
    public ResponseEntity<JwtAuthenticationResponse> refresh(@RequestBody RefreshTokenRequest refreshTokenRequest)  {
        return ResponseEntity.ok(authenticationService.refreshToken(refreshTokenRequest));
    }

}