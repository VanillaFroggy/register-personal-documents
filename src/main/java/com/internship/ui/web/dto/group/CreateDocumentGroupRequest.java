package com.internship.ui.web.dto.group;

import jakarta.validation.constraints.NotBlank;

public record CreateDocumentGroupRequest(
        @NotBlank String name,
        @NotBlank String color
) {
}
