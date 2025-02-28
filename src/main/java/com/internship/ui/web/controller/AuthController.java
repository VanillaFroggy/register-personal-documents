package com.internship.ui.web.controller;

import com.internship.persistence.entity.User;
import com.internship.service.AuthService;
import com.internship.service.DocumentService;
import com.internship.ui.web.dto.auth.RegisterRequest;
import com.internship.ui.web.mapper.AuthWebMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final DocumentService documentService;
    private final AuthWebMapper mapper;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/success")
    public String loginSuccess(HttpServletResponse response) {
        Long userId = ((User) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal())
                .getId();
        Cookie cookie = new Cookie(
                "hasDocumentsToRenew",
                String.valueOf(documentService.hasDocumentsToRenew(userId))
        );
        cookie.setPath("/");
        cookie.setHttpOnly(false);
        cookie.setMaxAge(60 * 60 * 24);
        response.addCookie(cookie);
        return String.format("redirect:/group/getAll?userId=%d&pageNumber=0&pageSize=50", userId);
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestBody RegisterRequest request) {
        authService.register(mapper.toDto(request));
        return "redirect:/auth/login";
    }
}
