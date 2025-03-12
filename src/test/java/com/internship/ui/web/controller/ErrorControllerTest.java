package com.internship.ui.web.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ErrorControllerTest {
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        ThymeleafViewResolver resolver = new ThymeleafViewResolver();
        resolver.setTemplateEngine(new SpringTemplateEngine());
        resolver.setCharacterEncoding("UTF-8");

        mockMvc = MockMvcBuilders.standaloneSetup(new ErrorController())
                .setViewResolvers(resolver)
                .build();
    }

    @Test
    void error() throws Exception {
        String errorMessage = "errorMessage";
        mockMvc.perform(get("/error")
                .sessionAttr("errorMessage", errorMessage))
                .andExpect(status().isOk())
                .andExpect(view().name("error"))
                .andExpect(model().attribute("errorMessage", errorMessage));
    }
}