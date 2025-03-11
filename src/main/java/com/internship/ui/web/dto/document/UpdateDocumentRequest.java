package com.internship.ui.web.dto.document;

import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.ZonedDateTime;

public record UpdateDocumentRequest(
        @NotNull
        @Positive
        Long id,

        @NotBlank
        String title,

        @NotNull
        @PastOrPresent
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        ZonedDateTime dateOfIssue,

        @NotNull
        @Future
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        ZonedDateTime expirationDate,

        @NotNull
        @Positive
        Long documentTypeId,

        @NotNull
        @Positive
        Long documentGroupId
) {
}
