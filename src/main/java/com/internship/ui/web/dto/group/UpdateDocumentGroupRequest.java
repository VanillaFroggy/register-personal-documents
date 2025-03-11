package com.internship.ui.web.dto.group;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record UpdateDocumentGroupRequest(
        @NotNull
        @Positive
        Long id,

        @NotBlank
        String name,

        @NotBlank
        String color
) {
}
