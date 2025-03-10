package com.internship.ui.web.dto.group;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record UpdateDocumentGroupRequest(
        @NotNull
        @Positive
        Long id,

        @NotEmpty
        String name,

        @NotEmpty
        String color
) {
}
