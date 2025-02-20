package com.internship.ui.web.dto.type;

public record CreateDocumentTypeRequest(
        String name,
        int daysBeforeExpirationToWarnUser
) {
}
