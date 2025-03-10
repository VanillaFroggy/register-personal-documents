package com.internship.ui.web.dto.group;

import jakarta.validation.constraints.NotEmpty;

public record CreateDocumentGroupRequest(
        @NotEmpty String name,
        @NotEmpty String color
) {
}
