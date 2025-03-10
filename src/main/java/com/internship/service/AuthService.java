package com.internship.service;

import com.internship.service.dto.auth.RegisterDto;
import com.internship.service.exceptoin.NotFoundException;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AuthService extends UserDetailsService {
    void register(RegisterDto dto) throws NotFoundException;
}
