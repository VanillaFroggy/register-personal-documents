package com.internship.ui.web.dto.document;

import java.time.ZonedDateTime;

public record CreateDocumentRequest(
        Long userId,
        String title,
        Long documentTypeId,
        Long documentGroupId,
        ZonedDateTime expirationDate
) {
}
