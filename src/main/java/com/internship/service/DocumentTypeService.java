package com.internship.service;

import com.internship.service.dto.type.CreateDocumentTypeDto;
import com.internship.service.dto.type.DocumentTypeDto;
import com.internship.service.dto.type.UpdateDocumentTypeDto;
import com.internship.service.exceptoin.NotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
public interface DocumentTypeService {
    List<DocumentTypeDto> getAllDocumentTypes();

    List<DocumentTypeDto> getPageOfTypes(@Min(0) int pageNumber, @Min(1) @Max(50) int pageSize);

    DocumentTypeDto getTypeById(@NotNull Long id) throws NotFoundException;

    DocumentTypeDto addType(@Valid CreateDocumentTypeDto dto);

    DocumentTypeDto updateType(@Valid UpdateDocumentTypeDto dto);

    void deleteType(@NotNull Long id);
}
