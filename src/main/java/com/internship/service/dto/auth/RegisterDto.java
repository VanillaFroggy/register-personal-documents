package com.internship.service.dto.auth;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record RegisterDto(
        @NotNull
        @Pattern(regexp = "^\\w{4,32}$")
        String username,

        @NotNull
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[\\w\\s]).{12,50}")
        String password
) {
}
