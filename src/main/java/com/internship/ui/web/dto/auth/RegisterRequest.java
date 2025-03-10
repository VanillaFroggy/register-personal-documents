package com.internship.ui.web.dto.auth;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

public record RegisterRequest(
        @NotEmpty
        @Pattern(regexp = "^\\w{4,32}$")
        String username,

        @NotEmpty
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[\\w\\s]).{12,50}")
        String password
) {
}
