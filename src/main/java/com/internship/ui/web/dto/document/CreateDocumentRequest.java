package com.internship.ui.web.dto.document;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.ZonedDateTime;

public record CreateDocumentRequest(
        @NotEmpty
        String title,

        @NotNull
        @Positive
        Long documentTypeId,

        @NotNull
        @Positive
        Long documentGroupId,

        @NotNull
        @Future
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        ZonedDateTime expirationDate
) {
}
