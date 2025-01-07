package com.animax.animax.entities;

import com.animax.animax.enums.Role;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Data
@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @NotBlank
    @Column(unique = true)
    @Size(min = 3, max = 100, message = "Username must be between 3 and 100 characters long")
//    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Invalid username format. Only alphanumeric characters and underscore are allowed.")
    private String username;

    @NotNull
    @NotBlank(message = "Email must not be blank")
    @Column(unique = true)
    private String email;

    @NotNull
    @NotBlank
    @Column(unique = true, length = 13)
//    @Pattern(regexp = "^(\\+20)?01[0-2]{1}[0-9]{8}$", message = "Invalid Egyptian phone number")
    private String phoneNumber;

    @NotBlank
    @Column(nullable = false)
    @Size(min = 6, message = "Password must be more than or equal 6 characters long")
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    private LocalDateTime createdAt;

    @JsonManagedReference
    @OneToMany(mappedBy = "user",cascade = CascadeType.REMOVE)
    private List<Token> tokens;

    @PrePersist
    protected void onCreate() {
        role = Role.USER;
        createdAt = LocalDateTime.now();
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}