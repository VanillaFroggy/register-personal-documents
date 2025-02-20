package com.internship.service.dto.user;

import jakarta.validation.constraints.NotNull;

public record RegisterUserDto(
        @NotNull String username,
        @NotNull String password
) {
}
