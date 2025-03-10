package com.internship.service.dto.document;

import java.time.ZonedDateTime;

public record CreateDocumentDto(
        String title,
        Long documentTypeId,
        Long documentGroupId,
        ZonedDateTime expirationDate
) {
}
