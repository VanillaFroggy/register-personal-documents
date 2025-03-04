package com.internship.service;

import com.internship.service.dto.document.CreateDocumentDto;
import com.internship.service.dto.document.DocumentDto;
import com.internship.service.dto.document.UpdateDocumentDto;
import com.internship.service.exceptoin.AccessException;
import com.internship.service.exceptoin.NotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
public interface DocumentService {
    boolean hasDocumentsToRenew();

    List<DocumentDto> getPageOfDocumentsByGroup(
            @NotNull Long groupId,
            @Min(0) int pageNumber,
            @Min(1) @Max(50) int pageSize
    );

    List<DocumentDto> getAllDocumentsToRenew();

    DocumentDto getDocumentById(@NotNull Long id) throws AccessException, NotFoundException;

    DocumentDto addDocument(@Valid CreateDocumentDto dto) throws NotFoundException;

    DocumentDto updateDocument(@Valid UpdateDocumentDto dto) throws AccessException, NotFoundException;

    void deleteDocument(@NotNull Long id) throws AccessException, NotFoundException;
}
