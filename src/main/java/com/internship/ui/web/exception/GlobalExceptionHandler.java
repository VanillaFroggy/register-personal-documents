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
        if (e.getMessage() != null) {
            session.setAttribute("errorMessage", e.getMessage());
        } else {
            session.setAttribute("errorMessage", e.getCause().getMessage());
        }
        return "redirect:/error";
    }
}
