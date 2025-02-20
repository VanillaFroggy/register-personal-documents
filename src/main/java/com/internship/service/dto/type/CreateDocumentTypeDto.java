package com.internship.service.dto.type;

import jakarta.validation.constraints.NotNull;

public record CreateDocumentTypeDto(
        @NotNull String name,
        int daysBeforeExpirationToWarnUser
) {
}
