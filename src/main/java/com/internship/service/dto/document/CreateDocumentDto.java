package com.internship.service.dto.document;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.ZonedDateTime;

public record CreateDocumentDto(
        @NotNull Long userId,
        @NotNull String title,
        @NotNull Long documentTypeId,
        @NotNull Long documentGroupId,
        @Future ZonedDateTime expirationDate
) {
}
