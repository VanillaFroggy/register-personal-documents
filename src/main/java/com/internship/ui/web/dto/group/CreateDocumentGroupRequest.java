package com.internship.ui.web.dto.group;

public record CreateDocumentGroupRequest(
        Long userId,
        String name,
        String color
) {
}
