package com.internship.ui.web.dto.type;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;

public record CreateDocumentTypeRequest(
        @NotEmpty String name,
        @Positive int daysBeforeExpirationToWarnUser
) {
}
