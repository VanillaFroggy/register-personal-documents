package com.internship.ui.web.dto.type;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record CreateDocumentTypeRequest(
        @NotBlank String name,
        @Positive int daysBeforeExpirationToWarnUser
) {
}
