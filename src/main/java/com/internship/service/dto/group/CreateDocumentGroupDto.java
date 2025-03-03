package com.internship.service.dto.group;


import jakarta.validation.constraints.NotNull;

public record CreateDocumentGroupDto(
        @NotNull String name,
        @NotNull String color
) {
}
