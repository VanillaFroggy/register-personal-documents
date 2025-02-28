package com.internship.service;

import com.internship.service.dto.document.CreateDocumentDto;
import com.internship.service.dto.document.DocumentDto;
import com.internship.service.dto.document.UpdateDocumentDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
public interface DocumentService {
    boolean hasDocumentsToRenew(Long userId);

    List<DocumentDto> getPageOfDocumentsByGroup(
            @NotNull Long userId,
            @NotNull Long groupId,
            @Min(0) int pageNumber,
            @Min(1) @Max(50) int pageSize
    );

    List<DocumentDto> getAllDocumentsToRenew(Long userId);

    DocumentDto getDocumentById(Long id);

    DocumentDto addDocument(@Valid CreateDocumentDto dto);

    DocumentDto updateDocument(@Valid UpdateDocumentDto dto);

    void deleteDocument(Long id);
}
