package com.internship.service.dto.type;

public record DocumentTypeDto(
        Long id,
        String name,
        int daysBeforeExpirationToWarnUser
) {
}
