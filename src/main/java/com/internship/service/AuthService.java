package com.internship.service;

import com.internship.service.dto.auth.RegisterDto;
import com.internship.service.dto.user.UserDto;
import jakarta.validation.Valid;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.annotation.Validated;

@Validated
public interface AuthService extends UserDetailsService {
    UserDto register(@Valid RegisterDto dto);
}
