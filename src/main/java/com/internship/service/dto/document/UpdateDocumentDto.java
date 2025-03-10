package com.internship.service.dto.document;

import java.time.ZonedDateTime;

public record UpdateDocumentDto(
        Long id,
        String title,
        ZonedDateTime dateOfIssue,
        ZonedDateTime expirationDate,
        Long documentTypeId,
        Long documentGroupId
) {
}
