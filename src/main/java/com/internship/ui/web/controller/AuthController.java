package com.internship.ui.web.controller;

import com.internship.service.AuthService;
import com.internship.service.DocumentService;
import com.internship.service.exceptoin.NotFoundException;
import com.internship.ui.web.dto.auth.RegisterRequest;
import com.internship.ui.web.mapper.AuthWebMapper;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Validated
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
    public String loginSuccess(HttpSession session) {
        session.setAttribute("hasDocumentsToRenew", documentService.hasDocumentsToRenew());
        return "redirect:/group/getAll?pageNumber=0&pageSize=50";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestBody @Valid RegisterRequest request) throws NotFoundException {
        authService.register(mapper.toDto(request));
        return "redirect:/auth/login";
    }
}
