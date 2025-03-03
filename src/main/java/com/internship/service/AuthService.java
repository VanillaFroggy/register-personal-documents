package com.internship.service;

import com.internship.service.dto.auth.RegisterDto;
import jakarta.validation.Valid;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.annotation.Validated;

@Validated
public interface AuthService extends UserDetailsService {
    void register(@Valid RegisterDto dto);
}
