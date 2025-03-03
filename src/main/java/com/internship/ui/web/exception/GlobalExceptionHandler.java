package com.internship.ui.web.exception;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public String handleException(
            Exception e,
            HttpSession session
    ) {
        session.setAttribute("errorMessage", e.getMessage());
        return "redirect:/error";
    }
}
