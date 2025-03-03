package com.internship.ui.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

@Controller
public class ErrorController {
    @GetMapping("/error")
    public String error(
            Model model,
            @SessionAttribute("errorMessage") String errorMessage
    ) {
        model.addAttribute("errorMessage", errorMessage);
        return "error";
    }
}
