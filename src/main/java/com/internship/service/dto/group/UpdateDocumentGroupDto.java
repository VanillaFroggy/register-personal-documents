package com.internship.service.dto.group;

import jakarta.validation.constraints.NotNull;

public record UpdateDocumentGroupDto(
        @NotNull Long id,
        @NotNull String name,
        @NotNull String color
) {
}
