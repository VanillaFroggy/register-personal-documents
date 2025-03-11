package com.internship.ui.web.dto.type;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record UpdateDocumentTypeRequest(
        @NotNull
        @Positive
        Long id,

        @NotBlank
        String name,

        @Positive
        int daysBeforeExpirationToWarnUser
) {
}
