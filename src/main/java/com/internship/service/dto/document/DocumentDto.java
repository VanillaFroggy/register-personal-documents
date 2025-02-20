package com.internship.service.dto.document;

import com.internship.service.dto.group.DocumentGroupDto;
import com.internship.service.dto.type.DocumentTypeDto;

import java.time.ZonedDateTime;

public record DocumentDto(
        Long id,
        String title,
        ZonedDateTime dateOfIssue,
        ZonedDateTime expirationDate,
        Long userId,
        DocumentTypeDto documentType,
        DocumentGroupDto documentGroup
) {
}
