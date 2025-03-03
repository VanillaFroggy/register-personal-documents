package com.internship.ui.web.dto.group;

public record UpdateDocumentGroupRequest(
        Long id,
        String name,
        String color
) {
}
