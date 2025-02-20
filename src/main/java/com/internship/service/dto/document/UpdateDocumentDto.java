package com.internship.service.dto.document;

import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.ZonedDateTime;

public record UpdateDocumentDto(
        @NotNull Long id,
        @NotNull String title,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime dateOfIssue,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime expirationDate,
        @NotNull Long userId,
        @NotNull Long documentTypeId,
        @NotNull Long documentGroupId
) {
}
