package com.internship.service.dto.type;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CreateDocumentTypeDto(
        @NotNull String name,
        @Min(1) int daysBeforeExpirationToWarnUser
) {
}
