package com.internship.ui.web.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record RegisterRequest(
        @NotBlank
        @Pattern(regexp = "^\\w{4,32}$")
        String username,

        @NotBlank
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[\\w\\s]).{12,50}")
        String password
) {
}
