package com.internship.ui.web.controller;

import com.internship.service.AuthService;
import com.internship.service.DocumentService;
import com.internship.service.dto.auth.RegisterDto;
import com.internship.ui.web.dto.auth.RegisterRequest;
import com.internship.ui.web.mapper.AuthWebMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {
    private static final String USERNAME = "username";
    private static final String PASSWORD = "Password123&";
    private static final String WRONG_USERNAME = "us";
    private static final String WRONG_PASSWORD = "password";

    @InjectMocks
    private AuthController authController;

    @Mock
    private AuthService authService;

    @Mock
    private DocumentService documentService;

    @Mock
    private AuthWebMapper mapper;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        ThymeleafViewResolver resolver = new ThymeleafViewResolver();
        resolver.setTemplateEngine(new SpringTemplateEngine());
        resolver.setCharacterEncoding("UTF-8");

        mockMvc = MockMvcBuilders.standaloneSetup(authController)
                .setViewResolvers(resolver)
                .setValidator(new LocalValidatorFactoryBean())
                .build();
    }

    @Test
    void login_shouldReturnLoginPage() throws Exception {
        mockMvc.perform(get("/auth/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    void loginSuccess_shouldRedirectToGroups_whenSuccessfullyLogin() throws Exception {
        when(documentService.hasDocumentsToRenew()).thenReturn(true);
        mockMvc.perform(get("/auth/success"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/group/getAll?pageNumber=0&pageSize=50"));
    }

    @Test
    void register_shouldReturnRegisterPage() throws Exception {
        mockMvc.perform(get("/auth/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"));
    }

    @Test
    void register_shouldRedirectToRegisterSuccess_whenSuccessfullyRegistered() throws Exception {
        RegisterRequest request = new RegisterRequest(USERNAME, PASSWORD);
        RegisterDto dto = new RegisterDto(USERNAME, PASSWORD);
        when(mapper.toDto(request)).thenReturn(dto);
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("""
                                        {
                                            "username": "%s",
                                            "password": "%s"
                                        }
                                        """,
                                request.username(), request.password())))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void register_shouldReturnBadRequest_whenUsernameIsNull() throws Exception {
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("""
                                        {
                                            "username": %s,
                                            "password": "%s"
                                        }
                                        """,
                                null, PASSWORD)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void register_shouldReturnBadRequest_whenUsernameIsEmpty() throws Exception {
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("""
                                        {
                                            "username": "%s",
                                            "password": "%s"
                                        }
                                        """,
                                "", PASSWORD)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void register_shouldReturnBadRequest_whenUsernameIsBlank() throws Exception {
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("""
                                        {
                                            "username": "%s",
                                            "password": "%s"
                                        }
                                        """,
                                " ", PASSWORD)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void register_shouldReturnBadRequest_whenUsernameDoesNotMatchPattern() throws Exception {
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("""
                                        {
                                            "username": "%s",
                                            "password": "%s"
                                        }
                                        """,
                                WRONG_USERNAME, PASSWORD)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void register_shouldReturnBadRequest_whenPasswordIsNull() throws Exception {
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("""
                                        {
                                            "username": "%s",
                                            "password": %s
                                        }
                                        """,
                                USERNAME, null)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void register_shouldReturnBadRequest_whenPasswordIsEmpty() throws Exception {
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("""
                                        {
                                            "username": "%s",
                                            "password": "%s"
                                        }
                                        """,
                                USERNAME, "")))
                .andExpect(status().isBadRequest());
    }

    @Test
    void register_shouldReturnBadRequest_whenPasswordIsBlank() throws Exception {
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("""
                                        {
                                            "username": "%s",
                                            "password": "%s"
                                        }
                                        """,
                                USERNAME, " ")))
                .andExpect(status().isBadRequest());
    }

    @Test
    void register_shouldReturnBadRequest_whenPasswordDoesNotMatchPattern() throws Exception {
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("""
                                        {
                                            "username": "%s",
                                            "password": "%s"
                                        }
                                        """,
                                USERNAME, WRONG_PASSWORD)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void registerSuccess_shouldReturnRegisterSuccessPage() throws Exception {
        mockMvc.perform(get("/auth/register-success"))
                .andExpect(status().isOk())
                .andExpect(view().name("register-success"));
    }
}