package com.internship.ui.web.dto.document;

import java.time.ZonedDateTime;

public record UpdateDocumentRequest(
        Long id,
        String title,
        ZonedDateTime dateOfIssue,
        ZonedDateTime expirationDate,
        Long userId,
        Long documentTypeId,
        Long documentGroupId
) {
}
