package com.internship.service.dto.type;

public record UpdateDocumentTypeDto(
        Long id,
        String name,
        int daysBeforeExpirationToWarnUser
) {
}
