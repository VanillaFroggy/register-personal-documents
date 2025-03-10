package com.internship.service.dto.type;

public record CreateDocumentTypeDto(
        String name,
        int daysBeforeExpirationToWarnUser
) {
}
