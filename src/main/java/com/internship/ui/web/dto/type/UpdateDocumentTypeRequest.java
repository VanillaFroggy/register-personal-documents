package com.internship.ui.web.dto.type;

public record UpdateDocumentTypeRequest(
        Long id,
        String name,
        int daysBeforeExpirationToWarnUser
) {
}
